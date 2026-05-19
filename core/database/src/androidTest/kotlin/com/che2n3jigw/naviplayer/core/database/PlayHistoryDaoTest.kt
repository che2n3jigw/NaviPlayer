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
// 创建时间： 2026/4/15 21:48
package com.che2n3jigw.naviplayer.core.database

import com.che2n3jigw.naviplayer.core.database.model.PlaybackEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.time.Clock

internal class PlayHistoryDaoTest : NaviPlayerDatabaseTest() {

    @Test
    fun getPlayHistory() = runTest {
        insertPlayHistory()
        val playHistory = playbackDao.getPlayHistory("guest").first()
        Assert.assertEquals(102, playHistory[0].duration)
    }


    private suspend fun insertPlayHistory() {
        playbackDao.upsertPlayHistory(
            PlaybackEntity(
                songId = "1",
                username = "guest",
                title = "title",
                artist = "artist",
                album = "album",
                coverArt = "coverArt",
                duration = 100,
                playedAt = Clock.System.now()
            )
        )
        playbackDao.upsertPlayHistory(
            PlaybackEntity(
                songId = "1",
                username = "guest",
                title = "title",
                artist = "artist",
                album = "album",
                coverArt = "coverArt",
                duration = 102,
                playedAt = Clock.System.now()
            )
        )
    }
}