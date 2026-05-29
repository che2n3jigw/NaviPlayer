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
// 创建时间： 2026/5/19 17:04
package com.che2n3jigw.naviplayer.feature.search.impl

import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.che2n3jigw.naviplayer.core.model.Song
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.core.ui.adapter.SelectableSongAdapter
import com.che2n3jigw.naviplayer.feature.player.api.widget.PlayerNavigator
import com.che2n3jigw.naviplayer.feature.search.impl.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var playerNavigator: PlayerNavigator

    private val viewModel: SearchViewModel by viewModels()

    private val selectableSongAdapter = SelectableSongAdapter()

    override fun inflateBinding(): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.rvSearchSongResult.apply {
            adapter = selectableSongAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = true
        }
    }

    override fun initListener() {
        binding.searchView.editText.setOnEditorActionListener { v, _, _ ->
            val txt = v.text.toString()
            binding.searchBar.setText(txt)
            binding.searchView.setText(txt)
            binding.searchView.hide()
            viewModel.search(txt)
            return@setOnEditorActionListener false
        }
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        selectableSongAdapter.itemClickListener = { song, _ ->
            viewModel.play(song)
        }
        binding.miniPlayer.onNextClick = {
            viewModel.skipToNext()
        }
        binding.miniPlayer.onPlayPauseClick = {
            viewModel.togglePlayPause()
        }
        binding.miniPlayer.onPreviousClick = {
            viewModel.skipToPrevious()
        }
        binding.miniPlayer.setOnClickListener {
            playerNavigator.navigateToPlayer(findNavController())
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.loading.isVisible = it is SearchUiState.Loading
                    binding.rvSearchSongResult.isVisible = it is SearchUiState.Success
                    if (it is SearchUiState.Success) {
                        selectableSongAdapter.submitList(it.songs)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playbackState.collect {
                    if (it != null) {
                        updateMiniPlayer(it.first, it.second)
                    }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.searchBar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }

    private fun updateMiniPlayer(song: Song?, isPlaying: Boolean) {
        binding.miniPlayer.updateSongInfo(
            song?.imageUrl ?: "", song?.name ?: "", song?.singer ?: ""
        )
        binding.miniPlayer.updatePlaying(isPlaying)
    }
}