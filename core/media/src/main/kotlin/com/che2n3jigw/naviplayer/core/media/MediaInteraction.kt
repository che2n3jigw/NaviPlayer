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
// 创建时间： 2026/5/25 15:09
package com.che2n3jigw.naviplayer.core.media

import com.che2n3jigw.naviplayer.core.model.Song

/**
 * 通用的播放控制行为
 */
interface MediaInteraction {
    /**
     * 播放特定歌曲（通常是点击列表项）
     */
    fun playSong(song: Song)

    /**
     * 切换播放/暂停
     */
    fun togglePlayPause()

    /**
     * 下一首
     */
    fun skipToNext()

    /**
     * 上一首
     */
    fun skipToPrevious()
}