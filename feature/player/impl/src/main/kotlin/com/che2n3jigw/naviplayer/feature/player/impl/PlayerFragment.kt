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
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.player.api.R
import com.che2n3jigw.naviplayer.feature.player.impl.databinding.FragmentPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 播放器页面
 */
@AndroidEntryPoint
class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {

    private val viewModel by viewModels<PlayerViewModel>()

    override fun inflateBinding() = FragmentPlayerBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.ivCover.load(it.currentSong?.imageUrl) {
                        error(com.che2n3jigw.naviplayer.core.ui.R.drawable.default_error_cover)
                    }
                    binding.tvSongName.text =
                        it.currentSong?.name ?: getString(R.string.player_song_name)
                    binding.tvSinger.text =
                        it.currentSong?.singer ?: getString(R.string.player_singer)
                    binding.mbFavourite.isChecked = it.currentSong?.isFavourite ?: false
                    binding.mbPlayPause.icon = if (it.isPlaying) {
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_music_pause)
                    } else {
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_music_play)
                    }
                }
            }
        }
    }
}