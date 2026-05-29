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
// 创建时间： 2026/5/25 15:42
package com.che2n3jigw.naviplayer.feature.songlist.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.media.api.PlayerController
import com.che2n3jigw.naviplayer.core.model.Song
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class SongListViewModel(
    protected val playerController: PlayerController
) : ViewModel(), PlayerController by playerController {

    /**
     * 歌曲列表页面相关UiState
     */
    abstract val songListUiState: StateFlow<SongListUiState>

    fun onSongClicked(song: Song) {
        viewModelScope.launch {
            val uiState = songListUiState.first()
            if (uiState is SongListUiState.Success) {
                val songs = uiState.songList.map { it.data }
                val index = songs.indexOf(song)
                if (index != -1) {
                    playerController.play(songs, index)
                }
            }
        }
    }
}