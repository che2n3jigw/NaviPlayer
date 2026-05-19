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
// 创建时间： 2026/5/19 15:13
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.naviplayer.core.database.dao.PlaybackDao
import com.che2n3jigw.naviplayer.core.database.model.PlaybackEntity
import com.che2n3jigw.naviplayer.core.model.Playback
import com.che2n3jigw.naviplayer.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Clock

/**
 * 默认播放记录仓库实现
 */
internal class DefaultPlaybackRepository @Inject constructor(
    private val playbackDao: PlaybackDao
) : PlaybackRepository {

    override fun getPlaybacks(username: String): Flow<List<Playback>> {
        return playbackDao.getPlaybacks(username).map { entities ->
            entities.map {
                Playback(
                    song = Song(
                        id = it.songId,
                        name = it.name,
                        singer = it.singer,
                        imageUrl = it.imageUrl,
                        streamUrl = it.streamUrl,
                    ),
                    playCount = it.playCount,
                    playedAt = it.playedAt.toEpochMilliseconds()
                )
            }
        }
    }

    override suspend fun upsertPlayback(username: String, song: Song) {
        val playback = playbackDao.getPlayback(song.id, username)
        playbackDao.upsertPlayback(
            PlaybackEntity(
                songId = song.id,
                username = username,
                name = song.name,
                singer = song.singer,
                imageUrl = song.imageUrl,
                streamUrl = song.streamUrl,
                playCount = (playback?.playCount ?: 0) + 1,
                playedAt = Clock.System.now()
            )
        )
    }
}