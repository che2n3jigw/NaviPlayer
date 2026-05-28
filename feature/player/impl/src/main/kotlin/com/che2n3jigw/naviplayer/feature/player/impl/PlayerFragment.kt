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

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.che2n3jigw.naviplayer.core.common.utils.TimeUtils
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.player.api.R
import com.che2n3jigw.naviplayer.feature.player.impl.databinding.FragmentPlayerBinding
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
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

    private val touchListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
            sliderTouchFlag = true
        }

        override fun onStopTrackingTouch(slider: Slider) {
            sliderTouchFlag = false
        }

    }

    override fun inflateBinding() = FragmentPlayerBinding.inflate(layoutInflater)

    override fun initView() {
        binding.slider.setLabelFormatter { value: Float ->
            timeUtils.toTimeText(value.toInt())
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
                            binding.slider.valueTo = duration.toFloat()
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
            }
        }
    }

    override fun onDestroyView() {
        binding.slider.removeOnSliderTouchListener(touchListener)
        super.onDestroyView()
    }
}