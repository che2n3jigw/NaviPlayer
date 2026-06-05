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
// 创建时间： 2026/6/5 15:51
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.naviplayer.core.database.dao.PlayListDao
import com.che2n3jigw.naviplayer.core.database.model.PlayListEntity
import com.che2n3jigw.naviplayer.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultPlayListRepository @Inject constructor(
    private val playListDao: PlayListDao
) : PlayListRepository {
    override val playList: Flow<List<Song>> = playListDao.getPlayList().map { entities ->
        entities.map {
            Song(
                id = it.songId,
                name = it.name,
                singer = it.singer,
                imageUrl = it.imageUrl,
                streamUrl = it.streamUrl,
                duration = it.duration,
                isFavourite = it.favourite
            )
        }
    }

    override suspend fun updatePlayList(songList: List<Song>) {
        val list = songList.map {
            PlayListEntity(
                songId = it.id,
                name = it.name,
                singer = it.singer,
                imageUrl = it.imageUrl,
                streamUrl = it.streamUrl,
                duration = it.duration,
                favourite = it.isFavourite
            )
        }
        playListDao.overwritePlayList(list)
    }

    override suspend fun clearPlayList() {
        playListDao.clearPlayList()
    }
}
