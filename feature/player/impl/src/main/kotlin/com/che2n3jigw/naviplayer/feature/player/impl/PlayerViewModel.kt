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
// 创建时间： 2026/5/27 22:40
package com.che2n3jigw.naviplayer.feature.player.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.common.utils.TimeUtils
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.media.api.PlayerController
import com.che2n3jigw.naviplayer.core.model.SelectableItem
import com.che2n3jigw.naviplayer.core.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val subsonicRepository: SubsonicRepository,
    private val naviMediaManager: NaviMediaManager,
    timeUtils: TimeUtils
) : ViewModel(), PlayerController by naviMediaManager {

    val uiState = combine(
        naviMediaManager.isPlaying,
        naviMediaManager.currentSong,
        naviMediaManager.position
    ) { isPlaying, currentSong, position ->
        // 总时长 单位秒
        val duration = currentSong?.duration ?: 0
        val durationTxt = timeUtils.toTimeText(duration)
        val currentDuration = (position / 1000).toInt()
        val currentDurationTxt = timeUtils.toTimeText(currentDuration)
        PlayerUiState(
            isPlaying,
            currentSong,
            duration,
            durationTxt,
            currentDuration,
            currentDurationTxt
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlayerUiState()
    )

    val playList = combine(
        naviMediaManager.playlist,
        naviMediaManager.currentSong
    ) { playlist, currentSong ->
        playlist.map {
            SelectableItem(it, it == currentSong)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun starOrUnStar() {
        viewModelScope.launch {
            naviMediaManager.currentSong.firstOrNull()?.let { song ->
                val isFavourite = song.isFavourite
                if (isFavourite) {
                    val success = subsonicRepository.unstar(song.id)
                    if (success) {
                        naviMediaManager.updateSongMetadata(song.id, false)
                    }
                } else {
                    val success = subsonicRepository.star(song.id)
                    if (success) {
                        naviMediaManager.updateSongMetadata(song.id, true)
                    }
                }
            }
        }
    }

    fun getSongId(): String {
        return uiState.value.currentSong?.id ?: ""
    }
}

data class PlayerUiState(
    val isPlaying: Boolean = false,
    val currentSong: Song? = null,
    /**
     * 歌曲时长 单位秒
     */
    val duration: Int = 0,
    val durationTxt: String = "",
    /**
     * 当前播放进度 单位秒
     */
    val currentDuration: Int = 0,
    val currentDurationTxt: String = ""
)