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
// 创建时间： 2026/4/27 11:52
package com.che2n3jigw.naviplayer.feature.library.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.media.api.PlaybackController
import com.che2n3jigw.naviplayer.core.model.SelectableItem
import com.che2n3jigw.naviplayer.core.model.Song
import com.che2n3jigw.naviplayer.core.ui.PageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    userRepository: UserRepository,
    private val subsonicRepository: SubsonicRepository,
    private val naviMediaManager: NaviMediaManager
) : ViewModel(), PlaybackController by naviMediaManager {
    var miniPlayerOut = false

    private val _randomSongs = MutableStateFlow<List<Song>?>(null)
    private val _playbackState =
        combine(naviMediaManager.currentSong, naviMediaManager.isPlaying) { song, isPlaying ->
            Pair(song, isPlaying)
        }

    val uiState = combine(
        userRepository.userData, _randomSongs, _playbackState
    ) { userData, randomSongs, playbackState ->
        // 检查登录状态
        if (!userData.isLoggedIn) {
            return@combine PageUiState.NotLogin
        }

        // 请求中
        if (randomSongs == null) {
            return@combine PageUiState.Loading
        }
        // 请求结束 没有数据
        if (randomSongs.isEmpty()) {
            return@combine PageUiState.Empty
        }

        val song = playbackState.first
        // 随机歌曲列表转换成可选中的随机歌曲列表
        val selectableRandomSongs = randomSongs.map {
            SelectableItem(it, it.id == song?.id)
        }
        LibraryUiState.Success(selectableRandomSongs, song, playbackState.second)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PageUiState.Loading
    )

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _randomSongs.update { subsonicRepository.getRandomSongs(30) }
        }
    }

    fun play(song: Song) {
        _randomSongs.value?.let {
            val index = it.indexOf(song)
            if (index != -1) {
                naviMediaManager.play(it, index)
            }
        }
    }
}

sealed interface LibraryUiState : PageUiState {
    /**
     * 加载成功
     */
    data class Success(
        val randomSongs: List<SelectableItem<Song>>,
        val currentSong: Song? = null,
        val isPlaying: Boolean = false
    ) : LibraryUiState
}
