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
// 创建时间： 2026/6/5 15:30
package com.che2n3jigw.naviplayer.core.database.model

import androidx.room.Entity

/**
 * 播放列表实体类
 */
@Entity(
    tableName = "play_list",
    primaryKeys = ["songId"]
)
data class PlayListEntity(
    /**
     * 歌曲唯一标识符 (Subsonic ID)
     */
    val songId: String,
    /**
     * 歌名
     */
    val name: String,
    /**
     * 歌手
     */
    val singer: String,
    /**
     * 封面
     */
    val imageUrl: String,
    /**
     * 音频地址
     */
    val streamUrl: String,
    /**
     * 时长
     */
    val duration: Int,
    /**
     * 是否收藏
     */
    val favourite: Boolean
)