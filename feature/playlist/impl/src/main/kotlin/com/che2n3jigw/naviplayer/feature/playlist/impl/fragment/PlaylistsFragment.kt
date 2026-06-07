/*
 * Copyright (c) 2026 che2n3jigw.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// 作者: che2n3jigw
// 邮箱: che2n3jigw@163.com
// 博客: che2n3jigw.github.io
// 创建时间： 2026/5/21 14:02
package com.che2n3jigw.naviplayer.feature.playlist.impl.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.playlist.api.PlaylistNavigator
import com.che2n3jigw.naviplayer.feature.playlist.impl.MarginItemDecoration
import com.che2n3jigw.naviplayer.feature.playlist.impl.R
import com.che2n3jigw.naviplayer.feature.playlist.impl.adapter.PlaylistsAdapter
import com.che2n3jigw.naviplayer.feature.playlist.impl.databinding.FragmentPlaylistsBinding
import com.che2n3jigw.naviplayer.feature.playlist.impl.viewmodel.PlaylistsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 歌单列表页
 */
@AndroidEntryPoint
class PlaylistsFragment : BaseFragment<FragmentPlaylistsBinding>() {

    @Inject
    lateinit var playlistNavigator: PlaylistNavigator

    private val viewModel: PlaylistsViewModel by viewModels()

    private val playlistsAdapter = PlaylistsAdapter()

    private var createPlaylistDialog: AlertDialog? = null
    private var deletePlaylistDialog: AlertDialog? = null

    override fun inflateBinding(): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.parseBundle(arguments)
        viewModel.queryPlaylists()
    }

    override fun initView() {
        binding.rvPlaylists.apply {
            adapter = playlistsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginItemDecoration(requireContext()))
        }
        binding.toolbar.inflateMenu(R.menu.playlists_top_appbar_menu)
    }

    override fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.playlists_top_appbar_item_add -> {
                    // 创建歌单弹窗
                    createPlaylistDialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.playlist_create)
                        .setView(R.layout.playlist_dialog_edit_text)
                        .setPositiveButton(R.string.playlist_enter) { dialog, _ ->
                            val alertDialog = (dialog as AlertDialog)
                            alertDialog.findViewById<TextView>(R.id.et_playlist_name)?.let { v ->
                                val name = v.text.toString()
                                viewModel.createPlaylist(name)
                            }
                        }
                        .setNegativeButton(R.string.playlist_cancel, null)
                        .show()
                    true
                }

                else -> false
            }
        }
        playlistsAdapter.onDeleteClickListener = { playlist ->
            deletePlaylistDialog = MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.playlist_delete_hint)
                .setPositiveButton(R.string.playlist_enter) { _, _ ->
                    viewModel.deletePlaylist(playlist)
                }
                .setNegativeButton(R.string.playlist_cancel, null)
                .show()
        }
        playlistsAdapter.onItemClickListener = { playlist ->
            val showBottomNav = arguments?.getBoolean("showBottomNav") ?: true
            playlistNavigator.navigateToPlaylist(findNavController(), playlist.id, showBottomNav)
        }
        playlistsAdapter.onAddClickListener = { playlist ->
            viewModel.addSong(playlist.id)
        }
        playlistsAdapter.onRemoveClickListener = { playlist ->
            viewModel.removeSong(playlist.id)
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playlists.collect {
                    playlistsAdapter.submitList(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createFailedEvent.collect {
                    Toast.makeText(
                        requireContext(),
                        R.string.playlist_create_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deleteFailedEvent.collect {
                    Toast.makeText(
                        requireContext(),
                        R.string.playlist_delete_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionFailed.collect {
                    Snackbar.make(
                        binding.root,
                        R.string.playlist_action_failed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        createPlaylistDialog?.dismiss()
        deletePlaylistDialog?.dismiss()
    }
}