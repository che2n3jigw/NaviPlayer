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
// 创建时间： 2026/5/26 11:41
package com.che2n3jigw.naviplayer.feature.album.api

/**
 * 专辑列表项的 UI 模型封装
 */
sealed interface AlbumItem {

    /**
     * 正常的专辑数据内容
     */
    data class Content(
        val id: String,
        val title: String,
        val coverUrl: String
    ) : AlbumItem

    /**
     * 列表末尾的“更多”按钮
     */
    data object More : AlbumItem
}