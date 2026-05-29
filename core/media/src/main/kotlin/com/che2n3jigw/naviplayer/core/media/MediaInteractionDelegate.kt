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
// 创建时间： 2026/5/25 15:01
package com.che2n3jigw.naviplayer.core.media

import com.che2n3jigw.naviplayer.core.model.Song

/**
 * 通用的播放控制行为委托类
 * @param naviMediaManager  媒体管理器
 * @param songListProvider  歌曲列表提供者
 */
class MediaInteractionDelegate(
    private val naviMediaManager: NaviMediaManager,
    private val songListProvider: () -> List<Song>
) : MediaInteraction {

    override fun togglePlayPause() {
        naviMediaManager.togglePlay()

    }

    override fun skipToNext() {
        naviMediaManager.playNext()
    }

    override fun skipToPrevious() {
        naviMediaManager.playPrevious()
    }

    override fun playSong(song: Song) {
        songListProvider.invoke().let {
            val index = it.indexOf(song)
            if (index != -1) {
                naviMediaManager.setMediaItems(it, index)
                naviMediaManager.play()
            }
        }
    }
}