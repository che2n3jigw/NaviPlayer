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
// 创建时间： 2026/5/19 15:34
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.naviplayer.core.model.Playback
import com.che2n3jigw.naviplayer.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A [UserPlaybackRepository] that delegates to two other repositories.
 */
class CompositeUserPlaybackRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val playbackRepository: PlaybackRepository
) : UserPlaybackRepository {

    override val playbacks: Flow<List<Playback>>
        get() = userRepository.userData
            .map { it.username }
            .distinctUntilChanged()
            .flatMapLatest {
                if (it.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    playbackRepository.getPlaybacks(it)
                }
            }

    override suspend fun upsertPlayback(song: Song) {
        val username = userRepository.userData.first().username
        playbackRepository.upsertPlayback(username, song)
    }
}