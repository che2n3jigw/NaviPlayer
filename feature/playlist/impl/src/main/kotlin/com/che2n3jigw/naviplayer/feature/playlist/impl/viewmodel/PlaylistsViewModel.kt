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
// 创建时间： 2026/5/21 15:57
package com.che2n3jigw.naviplayer.feature.playlist.impl.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.model.Playlist
import com.che2n3jigw.naviplayer.feature.playlist.impl.model.PlaylistItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val subsonicRepository: SubsonicRepository
) : ViewModel() {

    private val _playlists = MutableStateFlow<List<PlaylistItemUiModel>>(emptyList())
    val playlists = _playlists.asStateFlow()

    private val _createFailedEvent = MutableSharedFlow<Unit>()
    val createFailedEvent = _createFailedEvent.asSharedFlow()

    private val _deleteFailedEvent = MutableSharedFlow<Unit>()
    val deleteFailedEvent = _deleteFailedEvent.asSharedFlow()

    private val _songId = MutableStateFlow("")
    val actionFailed = MutableSharedFlow<Unit>()

    fun queryPlaylists() {
        viewModelScope.launch {
            val lists = subsonicRepository.getPlaylistList()
            _playlists.update {
                lists.map {
                    val songId = _songId.first()
                    if (songId.isEmpty()) {
                        PlaylistItemUiModel(
                            showAdd = false,
                            showRemove = false,
                            showDelete = true,
                            playlist = it
                        )
                    } else {
                        val hasAdd =
                            subsonicRepository.getPlaylist(it.id).any { song -> song.id == songId }
                        if (hasAdd) {
                            PlaylistItemUiModel(
                                showAdd = false,
                                showRemove = true,
                                showDelete = false,
                                playlist = it
                            )
                        } else {
                            PlaylistItemUiModel(
                                showAdd = true,
                                showRemove = false,
                                showDelete = false,
                                playlist = it
                            )
                        }
                    }
                }
            }
        }
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            val playlist = subsonicRepository.createPlaylist(name)
            if (playlist == null) {
                _createFailedEvent.emit(Unit)
            } else {
                queryPlaylists()
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val success = subsonicRepository.deletePlaylist(playlist.id)
            if (success) {
                queryPlaylists()
            } else {
                _deleteFailedEvent.emit(Unit)
            }
        }
    }

    fun addSong(playlistId: String) {
        viewModelScope.launch {
            val success = subsonicRepository.addSongToPlaylist(playlistId, _songId.first())
            if (!success) {
                actionFailed.emit(Unit)
            } else {
                queryPlaylists()
            }
        }
    }

    fun removeSong(playlistId: String) {
        viewModelScope.launch {
            val success = subsonicRepository.removeSongFromPlaylist(playlistId, _songId.first())
            if (!success) {
                actionFailed.emit(Unit)
            } else {
                queryPlaylists()
            }
        }
    }

    fun parseBundle(bundle: Bundle?) {
        _songId.update { bundle?.getString("id") ?: "" }
    }
}