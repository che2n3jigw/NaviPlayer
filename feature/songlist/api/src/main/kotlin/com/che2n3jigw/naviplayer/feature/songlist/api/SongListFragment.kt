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
// 创建时间： 2026/5/25 14:14
package com.che2n3jigw.naviplayer.feature.songlist.api

import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.core.ui.adapter.SelectableSongAdapter
import com.che2n3jigw.naviplayer.feature.player.api.widget.PlayerNavigator
import com.che2n3jigw.naviplayer.feature.songlist.api.databinding.FragmentSongListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 歌曲列表页面
 */
@AndroidEntryPoint
abstract class SongListFragment : BaseFragment<FragmentSongListBinding>() {

    @Inject
    lateinit var playerNavigator: PlayerNavigator

    abstract val songListViewModel: SongListViewModel

    abstract fun getTitleRes(): Int

    private val selectableSongAdapter = SelectableSongAdapter()

    override fun inflateBinding(): FragmentSongListBinding {
        return FragmentSongListBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.rvSong.apply {
            adapter = selectableSongAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.toolbar.setTitle(getTitleRes())
    }

    override fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.miniPlayer.onPlayPauseClick = {
            songListViewModel.togglePlayPause()
        }
        binding.miniPlayer.onPreviousClick = {
            songListViewModel.skipToPrevious()
        }
        binding.miniPlayer.onNextClick = {
            songListViewModel.skipToNext()
        }
        selectableSongAdapter.itemClickListener = { song, _ ->
            songListViewModel.onSongClicked(song)
        }
        binding.miniPlayer.setOnClickListener {
            // 进入播放详情页
            playerNavigator.navigateToPlayer(findNavController())
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                songListViewModel.songListUiState.collect {
                    binding.loading.isVisible = it is SongListUiState.Loading
                    binding.rvSong.isVisible = it is SongListUiState.Success

                    if (it is SongListUiState.Success) {
                        selectableSongAdapter.submitList(it.songList)
                        updateMiniPlayerSection(it)
                    }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }

    open fun updateSubTitle(subTitle: Int) {
        binding.toolbar.setSubtitle(subTitle)
    }

    private fun updateMiniPlayerSection(state: SongListUiState.Success) {
        binding.miniPlayer.updateSongInfo(
            state.currentSong?.imageUrl ?: "",
            state.currentSong?.name ?: "",
            state.currentSong?.singer ?: ""
        )
        binding.miniPlayer.updatePlaying(state.isPlaying)
    }
}