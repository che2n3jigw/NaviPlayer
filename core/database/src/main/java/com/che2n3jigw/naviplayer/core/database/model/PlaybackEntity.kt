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
// 创建时间： 2026/4/15 17:11
package com.che2n3jigw.naviplayer.core.database.model

import androidx.room.Entity
import kotlin.time.Instant

/**
 * 播放历史数据结构
 */
@Entity(
    tableName = "playback",
    primaryKeys = ["songId", "username"]
)
data class PlaybackEntity(
    /**
     * 歌曲唯一标识符 (Subsonic ID)
     */
    val songId: String,
    /**
     * 用户名
     */
    val username: String,
    /**
     * 歌曲标题
     */
    val title: String,
    /**
     * 歌手名称
     */
    val artist: String,
    /**
     * 专辑名称
     */
    val album: String,
    /**
     * 封面图片 ID 或完整 URL
     */
    val coverArt: String,
    /**
     * 歌曲总时长（单位：秒）
     */
    val duration: Int,
    /**
     * 该歌曲被完整播放的总次数
     */
    val playCount: Int = 1,
    /**
     * 最后一次播放的具体时间戳
     * 注意：使用 Instant 需要在 Room Database 中配置 TypeConverter
     */
    val playedAt: Instant
)