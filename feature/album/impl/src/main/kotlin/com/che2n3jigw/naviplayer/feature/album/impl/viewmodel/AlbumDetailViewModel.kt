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

package com.che2n3jigw.naviplayer.feature.album.impl.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.SelectableItem
import com.che2n3jigw.naviplayer.feature.songlist.api.BaseSongListViewModel
import com.che2n3jigw.naviplayer.feature.songlist.api.SongListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    naviMediaManager: NaviMediaManager,
    private val subsonicRepository: SubsonicRepository
) : BaseSongListViewModel(naviMediaManager) {

    // 专辑id
    private val _albumId = MutableStateFlow("")

    override val uiState: StateFlow<SongListUiState> = _albumId.map { albumId ->
        if (albumId.isEmpty()) {
            SongListUiState.Loading
        } else {
            val songs = subsonicRepository.getAlbumDetail(albumId).map {
                SelectableItem(it, false)
            }
            SongListUiState.Success(songs)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SongListUiState.Loading
    )

    fun parseBundle(bundle: Bundle?) {
        _albumId.update { bundle?.getString("id") ?: "" }
    }
}