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
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    // 收藏歌曲
    private val _favouriteSongs = MutableStateFlow<List<String>>(emptyList())

    // 最近播放
    private val _recentlyPlayed = MutableStateFlow<List<String>>(emptyList())

    // 当前播放歌曲封面
    private val _currentPlaySong = MutableStateFlow<Any?>(null)

    val uiState: StateFlow<MeUiState> = combine(
        userRepository.userData,
        _favouriteSongs,
        _recentlyPlayed,
        _currentPlaySong
    ) { userData, favouriteSongs, recentlyPlayed, currentPlaySong ->
        MeUiState(
            isLoggedIn = userData.isLoggedIn,
            avatar = "",
            favouriteCover = favouriteSongs.firstOrNull() ?: "",
            lastPlayTime = recentlyPlayed.firstOrNull() ?: "",
            currentPlayCover = currentPlaySong?.toString() ?: "",
            currentPlayName = "",
            currentPlaySinger = ""
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MeUiState()
    )
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
     * 上一次听歌的时间
     */
    val lastPlayTime: String = "",
    /**
     * 专辑个数
     */
    val albumCount: Int = 0,
    /**
     * 歌手个数
     */
    val artistCount: Int = 0,
    /**
     * 歌单个数
     */
    val listCount: Int = 0,
    /**
     * 离线缓存大小
     */
    val offlineSize: String = "",
    /**
     * 当前播放歌曲封面
     */
    val currentPlayCover: String = "",
    /**
     * 当前播放歌曲名
     */
    val currentPlayName: String = "",
    /**
     * 当前播放歌手
     */
    val currentPlaySinger: String = ""
)
