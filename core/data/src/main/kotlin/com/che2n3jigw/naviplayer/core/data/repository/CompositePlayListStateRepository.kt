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
// 创建时间： 2026/6/5 15:27
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.naviplayer.core.model.PlayListState
import com.che2n3jigw.naviplayer.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class CompositePlayListStateRepository @Inject constructor(
    val userRepository: UserRepository,
    val playListRepository: PlayListRepository
) : PlayListStateRepository {

    override val playListState: Flow<PlayListState> =
        combine(userRepository.userData, playListRepository.playList) { user, list ->
            val song = list.find { it.id == user.currentSongId }
            PlayListState(list, song)
        }

    override suspend fun updatePlayList(songList: List<Song>) {
        playListRepository.updatePlayList(songList)
    }

    override suspend fun updateCurrentSong(song: Song) {
        userRepository.updateCurrentSongId(song.id)
    }
}