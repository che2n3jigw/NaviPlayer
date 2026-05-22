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
// 创建时间： 2026/4/13 13:56
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.naviplayer.core.data.session.SubsonicSession
import com.che2n3jigw.naviplayer.core.model.Album
import com.che2n3jigw.naviplayer.core.model.Artist
import com.che2n3jigw.naviplayer.core.model.Playlist
import com.che2n3jigw.naviplayer.core.model.Song
import kotlinx.coroutines.flow.StateFlow

interface SubsonicRepository {

    /**
     * 当前活动的Session
     */
    val activeSession: StateFlow<SubsonicSession?>

    /**
     * 获取歌手列表
     */
    suspend fun getArtistList(): List<Artist>

    /**
     * 获取专辑列表
     */
    suspend fun getAlbumList(size: Int, offset: Int): List<Album>

    /**
     * 获取歌单列表
     */
    suspend fun getPlaylistList(): List<Playlist>

    /**
     * 获取收藏列表
     */
    suspend fun getFavouriteList(): List<Song>

    /**
     * 获取头像地址
     */
    fun getAvatarUrl(username: String): String

    /**
     * 获取随机歌歌曲
     */
    suspend fun getRandomSongs(size: Int): List<Song>

    /**
     * 查询歌曲
     */
    suspend fun search(query: String): List<Song>

    /**
     * 创建歌单
     */
    suspend fun createPlaylist(name: String): Playlist?

    /**
     * 删除歌单
     */
    suspend fun deletePlaylist(id: String): Boolean
}