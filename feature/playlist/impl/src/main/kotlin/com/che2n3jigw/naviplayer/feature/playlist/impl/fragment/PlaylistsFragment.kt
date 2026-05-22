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
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.playlist.impl.MarginItemDecoration
import com.che2n3jigw.naviplayer.feature.playlist.impl.adapter.PlaylistsAdapter
import com.che2n3jigw.naviplayer.feature.playlist.impl.databinding.FragmentPlaylistsBinding
import com.che2n3jigw.naviplayer.feature.playlist.impl.viewmodel.PlaylistsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 歌单列表页
 */
@AndroidEntryPoint
class PlaylistsFragment : BaseFragment<FragmentPlaylistsBinding>() {

    private val viewModel: PlaylistsViewModel by viewModels()

    private val playlistsAdapter = PlaylistsAdapter()

    override fun inflateBinding(): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.queryPlaylists()
    }

    override fun initView() {
        binding.rvPlaylists.apply {
            adapter = playlistsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginItemDecoration(requireContext()))
        }
    }

    override fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
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
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }
}