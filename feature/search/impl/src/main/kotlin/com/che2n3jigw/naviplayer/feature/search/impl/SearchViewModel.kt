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
// 创建时间： 2026/5/20 11:08
package com.che2n3jigw.naviplayer.feature.search.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.SelectableSong
import com.che2n3jigw.naviplayer.core.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val subsonicRepository: SubsonicRepository,
    private val naviMediaManager: NaviMediaManager
) : ViewModel() {

    private val _searchResult = MutableStateFlow<List<Song>>(emptyList())
    private val _loading = MutableStateFlow(false)

    val uiState = combine(_loading, _searchResult) { loading, result ->
        if (loading) {
            return@combine SearchUiState.Loading
        }
        if (result.isEmpty()) {
            return@combine SearchUiState.Empty
        }
        val songs = result.map {
            SelectableSong(it, false)
        }
        SearchUiState.Success(songs)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState.Loading
    )

    val playbackState = combine(
        naviMediaManager.currentSong, naviMediaManager.isPlaying
    ) { song, isPlaying ->
        Pair(song, isPlaying)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun search(query: String) {
        viewModelScope.launch {
            val result = subsonicRepository.search(query)
            _searchResult.update { result }
        }
    }

    /**
     * 播放歌曲
     */
    fun play(song: Song) {
        viewModelScope.launch {
            val list = _searchResult.first()
            val position = list.indexOf(song)
            if (position != -1) {
                naviMediaManager.setMediaItems(list, position)
                naviMediaManager.play()
            }
        }
    }

    fun playNext() {
        viewModelScope.launch {
            naviMediaManager.playNext()
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
}

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data object Empty : SearchUiState
    data class Success(val songs: List<SelectableSong>) : SearchUiState
}