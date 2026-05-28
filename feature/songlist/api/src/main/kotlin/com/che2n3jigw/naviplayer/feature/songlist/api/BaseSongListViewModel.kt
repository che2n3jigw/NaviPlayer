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
import com.che2n3jigw.naviplayer.core.media.MediaInteractionDelegate
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.Song

abstract class BaseSongListViewModel(
    protected val naviMediaManager: NaviMediaManager
) : ViewModel(), SongListInteraction {

    // 在基类中统一创建委托实例
    // 使用 lazy 确保在访问时 viewModelScope 已经可用
    private val mediaDelegate by lazy {
        MediaInteractionDelegate(
            naviMediaManager = naviMediaManager,
            scope = viewModelScope,
            songListProvider = {
                // 统一从当前的 uiState 中提取歌曲列表
                (uiState.value as? SongListUiState.Success)?.songList?.map { it.data }
                    ?: emptyList()
            }
        )
    }

    override fun playSong(song: Song) = mediaDelegate.playSong(song)
    override fun togglePlayPause() = mediaDelegate.togglePlayPause()
    override fun skipToNext() = mediaDelegate.skipToNext()
    override fun skipToPrevious() = mediaDelegate.skipToPrevious()
}