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
// 创建时间： 2026/5/14 15:41
package com.che2n3jigw.naviplayer.feature.player.impl.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.load
import com.che2n3jigw.naviplayer.feature.player.impl.R
import com.che2n3jigw.naviplayer.feature.player.impl.databinding.ViewMiniPlayerBinding

/**
 * 迷你播放器
 */
class MiniPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding = ViewMiniPlayerBinding.inflate(LayoutInflater.from(context), this)

    var onPlayPauseClick: (() -> Unit)? = null
    var onPreviousClick: (() -> Unit)? = null
    var onNextClick: (() -> Unit)? = null

    init {
        binding.ivCoverPlaying.load(R.drawable.player_mini_player_cover)
        binding.mbPlay.setOnClickListener {
            onPlayPauseClick?.invoke()
        }
        binding.mbPrevious.setOnClickListener {
            onPreviousClick?.invoke()
        }
        binding.mbNext.setOnClickListener {
            onNextClick?.invoke()
        }
    }

    fun updateSongInfo(cover: String = "", name: String = "", singer: String = "") {
        binding.ivCoverPlaying.load(cover) { error(R.drawable.player_mini_player_cover) }
        binding.tvSingName.text = name
        binding.tvSinger.text = singer
    }

    fun updatePlaying(isPlaying: Boolean) {
        binding.mbPlay.icon = if (isPlaying) {
            ContextCompat.getDrawable(context, R.drawable.ic_music_pause)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_music_play)
        }
    }
}