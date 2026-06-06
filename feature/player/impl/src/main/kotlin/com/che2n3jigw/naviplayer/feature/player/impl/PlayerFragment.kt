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
// 创建时间： 2026/5/27 16:52
package com.che2n3jigw.naviplayer.feature.player.impl

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.che2n3jigw.naviplayer.core.common.utils.TimeUtils
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.core.ui.adapter.SelectableSongAdapter
import com.che2n3jigw.naviplayer.feature.player.api.R
import com.che2n3jigw.naviplayer.feature.player.impl.databinding.FragmentPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 播放器页面
 */
@AndroidEntryPoint
class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {

    @Inject
    lateinit var timeUtils: TimeUtils

    private val viewModel by viewModels<PlayerViewModel>()

    private var sliderTouchFlag = false

    private val selectableSongAdapter = SelectableSongAdapter()

    private val touchListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
            sliderTouchFlag = true
        }

        override fun onStopTrackingTouch(slider: Slider) {
            sliderTouchFlag = false
            viewModel.seekTo(slider.value.toInt() * 1000L)
        }
    }

    override fun inflateBinding() = FragmentPlayerBinding.inflate(layoutInflater)

    override fun initView() {
        binding.slider.setLabelFormatter { value: Float ->
            timeUtils.toTimeText(value.toInt())
        }
        binding.rvPlayList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = selectableSongAdapter
        }
    }

    override fun initListener() {
        binding.slider.addOnSliderTouchListener(touchListener)
        binding.mbPlayPause.setOnClickListener {
            viewModel.togglePlayPause()
        }
        binding.mbPrevious.setOnClickListener {
            viewModel.skipToPrevious()
        }
        binding.mbNext.setOnClickListener {
            viewModel.skipToNext()
        }
        binding.mbFavourite.setOnClickListener {
            viewModel.starOrUnStar()
        }
        binding.mbBack.setOnClickListener {
            findNavController().popBackStack()
        }
        selectableSongAdapter.itemClickListener = { _, index ->
            lifecycleScope.launch {
                viewModel.skipToItem(index)
                delay(200)
                viewModel.play()
            }
        }

        BottomSheetBehavior.from(binding.clSongList).addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        lifecycleScope.launch {
                            val list = viewModel.playList.value
                            val index = list.indexOfFirst { it.isSelected }
                            if (index != -1) {
                                (binding.rvPlayList.layoutManager as? LinearLayoutManager)
                                    ?.scrollToPositionWithOffset(index, 0)
                            }
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            }
        )
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState
                        .map { it.currentSong }
                        .distinctUntilChanged()
                        .collect { song ->
                            val songName = song?.name ?: getString(R.string.player_song_name)
                            val singer = song?.singer ?: getString(R.string.player_singer)
                            val isFavourite = song?.isFavourite ?: false
                            binding.ivCover.load(song?.imageUrl) {
                                error(com.che2n3jigw.naviplayer.core.ui.R.drawable.default_error_cover)
                            }
                            binding.tvSongName.text = songName
                            binding.tvSinger.text = singer
                            binding.mbFavourite.isChecked = isFavourite
                        }
                }

                launch {
                    viewModel.uiState
                        .map { it.duration to it.durationTxt }
                        .distinctUntilChanged()
                        .collect { (duration, durationTxt) ->
                            if (duration > 0.0) {
                                binding.slider.valueTo = duration.toFloat()
                                binding.slider.isEnabled = true
                            } else {
                                binding.slider.isEnabled = false
                            }
                            binding.tvSongDuration.text = durationTxt
                        }
                }

                launch {
                    viewModel.uiState
                        .map { it.currentDuration to it.currentDurationTxt }
                        .distinctUntilChanged()
                        .collect { (currentDuration, currentDurationTxt) ->
                            if (!sliderTouchFlag) {
                                binding.slider.value = currentDuration.toFloat()
                            }
                            binding.tvCurrentTime.text = currentDurationTxt
                        }
                }

                launch {
                    viewModel.uiState
                        .map { it.isPlaying }
                        .distinctUntilChanged()
                        .collect { isPlaying ->
                            val ctx = requireContext()
                            binding.mbPlayPause.icon = if (isPlaying) {
                                ContextCompat.getDrawable(ctx, R.drawable.ic_music_pause)
                            } else {
                                ContextCompat.getDrawable(ctx, R.drawable.ic_music_play)
                            }
                        }
                }

                launch {
                    viewModel.playList.collect {
                        selectableSongAdapter.submitList(it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.slider.removeOnSliderTouchListener(touchListener)
        super.onDestroyView()
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.mbBack.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }
}