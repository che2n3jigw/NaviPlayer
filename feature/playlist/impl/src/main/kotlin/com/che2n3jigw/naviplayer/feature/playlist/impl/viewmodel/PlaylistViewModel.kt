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
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.Song
import com.che2n3jigw.naviplayer.feature.songlist.api.SongListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val subsonicRepository: SubsonicRepository,
    naviMediaManager: NaviMediaManager
) : SongListViewModel(naviMediaManager) {

    private val _playlistId = MutableStateFlow<String?>(null)

    override val songList = _playlistId.flatMapLatest { id ->
        if (id == null) {
            flowOf(null)
        } else if (id.isEmpty()) {
            flowOf(emptyList<Song>())
        } else {
            flow { emit(subsonicRepository.getPlaylist(id)) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
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