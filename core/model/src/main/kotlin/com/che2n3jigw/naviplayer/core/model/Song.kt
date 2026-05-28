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
// 创建时间： 2026/4/13 14:32
package com.che2n3jigw.naviplayer.core.model

/**
 * 歌曲数据结构
 */
data class Song(
    val id: String = "",
    val name: String = "",
    val singer: String = "",
    val imageUrl: String = "",
    val streamUrl: String = "",
    val downloadUrl: String = "",
    val duration: Int = 0,
    val isFavourite: Boolean = false
)