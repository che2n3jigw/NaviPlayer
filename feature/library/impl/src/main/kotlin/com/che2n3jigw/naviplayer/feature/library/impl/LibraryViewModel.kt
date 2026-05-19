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
import com.che2n3jigw.naviplayer.core.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    userRepository: UserRepository,
    private val subsonicRepository: SubsonicRepository,
    private val naviMediaManager: NaviMediaManager
) : ViewModel() {
    var appBarLayoutOffset = 0
    var miniPlayerOut = false

    private val _albums = MutableStateFlow<List<LibraryItem>?>(null)
    private val _randomSongs = MutableStateFlow<List<Song>?>(null)
    private val _playbackState = combine(
        naviMediaManager.currentSong, naviMediaManager.isPlaying
    ) { song, isPlaying ->
        Pair(song, isPlaying)
    }

    val uiState: StateFlow<LibraryUiState> = combine(
        userRepository.userData, _albums, _randomSongs, _playbackState
    ) { userData, albums, randomSongs, playbackState ->
        if (!userData.isLoggedIn) {
            LibraryUiState.NotLogin
        } else if (albums == null || randomSongs == null) {
            LibraryUiState.Loading
        } else {
            LibraryUiState.Success(albums, randomSongs, playbackState.first, playbackState.second)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LibraryUiState.Loading
    )

    init {
        viewModelScope.launch {
            subsonicRepository.activeSession.collect {
                if (it != null) {
                    loadData()
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val albumsDeferred = async {
                subsonicRepository.getAlbumList(5, 0).map {
                    LibraryItem(it.id, it.name, it.imageUrl)
                }
            }

            val randomDeferred = async {
                subsonicRepository.getRandomSongs(30)
            }

            // 等待结果并赋值
            _albums.value = albumsDeferred.await()
            _randomSongs.value = randomDeferred.await()
        }
    }

    fun play(song: Song) {
        _randomSongs.value?.let {
            val index = it.indexOf(song)
            if (index != -1) {
                naviMediaManager.setMediaItems(it, index)
                naviMediaManager.togglePlay()
            }
        }
    }
}

sealed interface LibraryUiState {
    /**
     * 加载中
     */
    data object Loading : LibraryUiState

    /**
     * 未登录状态
     */
    data object NotLogin : LibraryUiState

    /**
     * 加载成功
     */
    data class Success(
        val albums: List<LibraryItem> = emptyList(),
        val randomSongs: List<Song> = emptyList(),
        val currentSong: Song? = null,
        val isPlaying: Boolean = false
    ) : LibraryUiState
}
