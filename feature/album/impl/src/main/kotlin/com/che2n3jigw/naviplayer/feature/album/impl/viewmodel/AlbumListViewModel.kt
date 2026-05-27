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
// 创建时间： 2026/5/27 11:13
package com.che2n3jigw.naviplayer.feature.album.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.feature.album.api.AlbumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val subsonicRepository: SubsonicRepository
) : ViewModel() {

    companion object {
        private const val SIZE = 500
    }

    private var offset = 0

    private val _uiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)

    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AlbumListUiState.Loading
    )

    fun loadData() {
        viewModelScope.launch {
            val albumContentList = subsonicRepository.getAlbumList(SIZE, offset).map {
                AlbumItem.Content(it.id, it.name, it.imageUrl)
            }
            val hasMore = albumContentList.size == SIZE
            if (hasMore) {
                offset += SIZE
            }
            _uiState.update { AlbumListUiState.Success(albumContentList, hasMore) }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val albumContentList = subsonicRepository.getAlbumList(SIZE, offset).map {
                AlbumItem.Content(it.id, it.name, it.imageUrl)
            }
            val hasMore = albumContentList.size == SIZE
            if (hasMore) {
                offset += SIZE
            }
            _uiState.update {
                if (it is AlbumListUiState.Success) {
                    AlbumListUiState.Success(it.albumContentList + albumContentList, hasMore)
                } else {
                    it
                }
            }
        }
    }
}

sealed interface AlbumListUiState {
    data object Loading : AlbumListUiState
    data class Success(
        val albumContentList: List<AlbumItem.Content>,
        val hasMore: Boolean
    ) : AlbumListUiState
}