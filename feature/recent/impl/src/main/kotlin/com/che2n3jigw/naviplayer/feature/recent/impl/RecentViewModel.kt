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
// 创建时间： 2026/5/25 22:35
package com.che2n3jigw.naviplayer.feature.recent.impl

import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.UserPlaybackRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.SelectableItem
import com.che2n3jigw.naviplayer.feature.songlist.api.BaseSongListViewModel
import com.che2n3jigw.naviplayer.feature.songlist.api.SongListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    naviMediaManager: NaviMediaManager,
    userPlaybackRepository: UserPlaybackRepository
) : BaseSongListViewModel(
    naviMediaManager
) {
    override val uiState: StateFlow<SongListUiState> =
        userPlaybackRepository.playbacks.map { playbacks ->
            val songList = playbacks.map {
                SelectableItem(it.song, false)
            }
            SongListUiState.Success(songList)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SongListUiState.Loading
        )
}