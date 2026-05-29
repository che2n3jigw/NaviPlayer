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
// 创建时间： 2026/5/29 11:26
package com.che2n3jigw.naviplayer.core.media.api

import com.che2n3jigw.naviplayer.core.model.Song

/**
 * 播放队列管理（改变播放上下文）
 * 适合：歌单列表、搜索列表、专辑列表
 */
interface QueueController {
    /**
     * 传入新的列表并从指定位置播放
     */
    fun play(songs: List<Song>, index: Int)

    /**
     * 在当前队列中直接跳转到某一项
     */
    fun skipToItem(index: Int)
}