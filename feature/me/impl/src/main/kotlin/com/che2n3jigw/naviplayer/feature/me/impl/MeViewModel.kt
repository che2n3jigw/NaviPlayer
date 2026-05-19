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
// 创建时间： 2026/4/8 15:59
package com.che2n3jigw.naviplayer.feature.me.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.common.utils.TimeUtils
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.data.repository.UserPlaybackRepository
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val subsonicRepository: SubsonicRepository,
    private val naviMediaManager: NaviMediaManager,
    private val timeUtils: TimeUtils,
    userPlaybackRepository: UserPlaybackRepository
) : ViewModel() {

    // 头像
    private val _avatar = MutableStateFlow("")

    // 收藏歌曲
    private val _favouriteSongs = MutableStateFlow<List<Song>>(emptyList())

    // 播放状态
    private val _playbackState =
        combine(naviMediaManager.currentSong, naviMediaManager.isPlaying) { song, playing ->
            Pair(song, playing)
        }

    val uiState: StateFlow<MeUiState> = combine(
        userRepository.userData,
        _favouriteSongs,
        userPlaybackRepository.playbacks,
        _avatar,
        _playbackState
    ) { userData, favouriteSongs, playbacks, avatar, playbackState ->
        val lastPlayback = playbacks.first()
        val lastPlaybackAt = lastPlayback.playedAt
        val lastPlaybackTime = if (lastPlaybackAt == 0L) {
            ""
        } else {
            timeUtils.toTimeAgo(lastPlaybackAt)
        }
        MeUiState(
            isLoggedIn = userData.isLoggedIn,
            avatar = avatar,
            favouriteCover = favouriteSongs.firstOrNull()?.imageUrl ?: "",
            favouriteCount = favouriteSongs.size,
            lastPlaybackTime = lastPlaybackTime,
            lastPlaybackCoverUrl = lastPlayback.song.imageUrl,
            currentSong = playbackState.first,
            isPlaying = playbackState.second
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MeUiState()
    )

    init {
        viewModelScope.launch {
            subsonicRepository.activeSession.collect { session ->
                if (session != null) {
                    refreshLibraryData()
                }
            }
        }
    }

    fun refreshLibraryData() {
        viewModelScope.launch {
            launch {
                val userName = userRepository.userData.first().username
                _avatar.value = subsonicRepository.getAvatarUrl(userName)
            }
            launch { _favouriteSongs.value = subsonicRepository.getFavouriteList() }
        }
    }

    fun playFavourite() {
        viewModelScope.launch {
            _favouriteSongs.firstOrNull()?.let {
                naviMediaManager.setMediaItems(it)
                naviMediaManager.play()
            }
        }
    }

    fun togglePlaying() {
        viewModelScope.launch {
            naviMediaManager.togglePlay()
        }
    }

    fun playPrevious() {
        viewModelScope.launch {
            naviMediaManager.playPrevious()
        }
    }

    fun playNext() {
        viewModelScope.launch {
            naviMediaManager.playNext()
        }
    }
}

data class MeUiState(
    /**
     * 是否登录
     */
    val isLoggedIn: Boolean = false,
    /**
     * 用户头像
     */
    val avatar: String = "",
    /**
     * 收藏歌曲封面
     */
    val favouriteCover: String = "",
    /**
     * 收藏歌曲个数
     */
    val favouriteCount: Int = 0,
    /**
     * 距离上一首歌曲播放时间
     */
    val lastPlaybackTime: String = "",
    /**
     * 上一首播放歌曲封面
     */
    val lastPlaybackCoverUrl: String = "",
    /**
     * 歌单个数
     */
    val listCount: Int = 0,
    /**
     * 离线缓存大小
     */
    val offlineSize: String = "",
    /**
     * 当前播放的歌曲信息（包含封面、名字、歌手）
     */
    val currentSong: Song? = null,
    /**
     * 是否正在播放（用于显示播放/暂停按钮图标）
     */
    val isPlaying: Boolean = false
)
