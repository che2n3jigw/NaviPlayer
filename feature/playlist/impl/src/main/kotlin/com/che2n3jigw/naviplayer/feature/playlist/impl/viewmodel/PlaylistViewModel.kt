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
// 创建时间： 2026/5/23 20:00
package com.che2n3jigw.naviplayer.feature.playlist.impl.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.model.SelectableSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val subsonicRepository: SubsonicRepository
) : ViewModel() {

    private val _playlistId = MutableStateFlow("")

    val uiState = _playlistId.map { id ->
        if (id.isEmpty()) {
            PlaylistUiState.Loading
        } else {
            val list = subsonicRepository.getPlaylist(id).map {
                SelectableSong(it, false)
            }
            PlaylistUiState.Success(list)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlaylistUiState.Loading
    )

    /**
     * 加载歌单数据
     */
    fun loadPlaylist(bundle: Bundle?) {
        bundle?.getString("id")?.let { id ->
            _playlistId.update { id }
        }
    }
}

sealed interface PlaylistUiState {
    data object Loading : PlaylistUiState
    data class Success(
        val songs: List<SelectableSong>
    ) : PlaylistUiState
}