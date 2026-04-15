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
// 创建时间： 2026/4/13 14:29
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.android.libs.opensubsonicapi.bean.AlbumListType
import com.che2n3jigw.naviplayer.core.data.session.SubsonicSessionManager
import com.che2n3jigw.naviplayer.core.model.Album
import com.che2n3jigw.naviplayer.core.model.Artist
import com.che2n3jigw.naviplayer.core.model.Playlist
import com.che2n3jigw.naviplayer.core.model.Song
import javax.inject.Inject


internal class DefaultSubsonicRepository @Inject constructor(
    private val subsonicSessionManager: SubsonicSessionManager
) : SubsonicRepository {

    override suspend fun getArtistList(): List<Artist> {
        return subsonicSessionManager.browsingDataSource?.getArtists()?.index
            ?.asSequence()
            ?.flatMap { it.artist ?: emptyList() }
            ?.map { Artist(it.id ?: "", it.name ?: "", it.artistImageUrl ?: "") }
            ?.toList() ?: emptyList()
    }

    override suspend fun getAlbumList(size: Int, offset: Int): List<Album> {
        return subsonicSessionManager.listsDataSource?.getAlbumList2(
            AlbumListType.AlphabeticalByName,
            size = size,
            offset = offset
        )?.asSequence()?.map {
            val coverUrl =
                subsonicSessionManager.mediaRetrievalDataSource?.getCoverArtUrl(it.id ?: "")
            Album(it.id ?: "", it.sortName ?: "", coverUrl ?: "")
        }?.toList() ?: emptyList()
    }

    override suspend fun getPlaylistList(): List<Playlist> {
        return subsonicSessionManager.playlistsDataSource?.getPlaylists()
            ?.asSequence()
            ?.map {
                Playlist(
                    it.id ?: "",
                    it.name ?: "",
                    it.songCount ?: 0,
                    it.owner ?: "",
                    it.changed ?: ""
                )
            }
            // 字符串直接比较即可，descending 表示降序（最新的在前）
            ?.sortedByDescending { it.changed }
            ?.toList() ?: emptyList()
    }

    override suspend fun getFavouriteList(): List<Song> {
        return subsonicSessionManager.listsDataSource?.getStarred2()?.song
            ?.asSequence()
            ?.map {
                val coverUrl =
                    subsonicSessionManager.mediaRetrievalDataSource?.getCoverArtUrl(it?.coverArt ?: "")
                Song(it?.id ?: "", it?.sortName ?: "", it?.displayArtist ?: "", coverUrl ?: "")
            }?.toList() ?: emptyList()
    }
}