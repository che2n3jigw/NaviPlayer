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
import com.che2n3jigw.android.libs.opensubsonicapi.response.common.Child
import com.che2n3jigw.naviplayer.core.data.session.SubsonicSessionManager
import com.che2n3jigw.naviplayer.core.model.Album
import com.che2n3jigw.naviplayer.core.model.Artist
import com.che2n3jigw.naviplayer.core.model.Playlist
import com.che2n3jigw.naviplayer.core.model.Song
import javax.inject.Inject


internal class DefaultSubsonicRepository @Inject constructor(
    private val subsonicSessionManager: SubsonicSessionManager
) : SubsonicRepository {

    override val activeSession = subsonicSessionManager.activeSession

    override suspend fun getArtistList(): List<Artist> {
        return subsonicSessionManager.browsingDataSource?.getArtists()?.index
            ?.asSequence()
            ?.flatMap { it.artist ?: emptyList() }
            ?.map {
                Artist(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    imageUrl = it.artistImageUrl ?: ""
                )
            }
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
            Album(
                id = it.id ?: "",
                name = it.sortName ?: "",
                imageUrl = coverUrl ?: ""
            )
        }?.toList() ?: emptyList()
    }

    override suspend fun getAlbumDetail(id: String): List<Song> {
        return subsonicSessionManager.browsingDataSource?.getAlbum(id)?.song?.map {
            childToSong(it)
        } ?: emptyList()
    }

    override suspend fun getPlaylistList(): List<Playlist> {
        return subsonicSessionManager.playlistsDataSource?.getPlaylists()
            ?.asSequence()
            ?.map {
                Playlist(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    songCount = it.songCount ?: 0,
                    owner = it.owner ?: "",
                    changed = it.changed ?: ""
                )
            }
            // 字符串直接比较即可，descending 表示降序（最新的在前）
            ?.sortedByDescending { it.changed }
            ?.toList() ?: emptyList()
    }

    override suspend fun getPlaylist(playlistId: String): List<Song> {
        return subsonicSessionManager.playlistsDataSource?.getPlaylist(playlistId)
            ?.entry
            ?.filterNotNull()
            ?.map {
                childToSong(it)
            } ?: emptyList()
    }

    override suspend fun getFavouriteList(): List<Song> {
        return subsonicSessionManager.listsDataSource?.getStarred2()?.song
            ?.asSequence()
            ?.filterNotNull()
            ?.map {
                childToSong(it)
            }?.toList() ?: emptyList()
    }

    override fun getAvatarUrl(username: String): String {
        return subsonicSessionManager.mediaRetrievalDataSource?.getAvatarUrl(username) ?: ""
    }

    override suspend fun getRandomSongs(size: Int): List<Song> {
        return subsonicSessionManager.listsDataSource?.getRandomSongs(size)?.map {
            childToSong(it)
        }?.toList() ?: emptyList()
    }

    override suspend fun search(query: String): List<Song> {
        return subsonicSessionManager.searchingDataSource?.search3(query)?.song
            ?.filterNotNull()
            ?.map { childToSong(it) }
            ?: emptyList()
    }

    override suspend fun createPlaylist(name: String): Playlist? {
        subsonicSessionManager.playlistsDataSource?.createPlaylist(name = name)?.let {
            return Playlist(
                id = it.id ?: "",
                name = it.name ?: "",
                songCount = it.songCount ?: 0,
                owner = it.owner ?: "",
                changed = it.changed ?: ""
            )
        }
        return null
    }

    override suspend fun deletePlaylist(id: String): Boolean {
        return subsonicSessionManager.playlistsDataSource?.deletePlaylist(id) ?: false
    }

    override suspend fun addSongToPlaylist(playlistId: String, songId: String): Boolean {
        return subsonicSessionManager.playlistsDataSource?.updatePlaylist(
            playlistId,
            songIdToAdd = listOf(songId)
        ) ?: false
    }

    override suspend fun removeSongFromPlaylist(playlistId: String, songId: String): Boolean {
        // 先获取歌单列表
        val playlist = getPlaylist(playlistId)
        val index = playlist.indexOfFirst { it.id == songId }
        if (index == -1) return false
        return subsonicSessionManager.playlistsDataSource?.updatePlaylist(
            playlistId,
            songIndexToRemove = listOf(index)
        ) ?: false
    }

    override suspend fun star(songId: String): Boolean {
        return subsonicSessionManager.mediaAnnotationDataSource?.star(id = listOf(songId)) ?: false
    }

    override suspend fun unstar(songId: String): Boolean {
        return subsonicSessionManager.mediaAnnotationDataSource
            ?.unstar(id = listOf(songId))
            ?: false
    }

    private fun childToSong(child: Child): Song {
        val id = child.id ?: ""
        val name = child.sortName ?: ""
        val singer = child.displayArtist ?: ""
        val duration = child.duration ?: 0
        var coverUrl = ""
        var streamUrl = ""
        var downloadUrl = ""
        subsonicSessionManager.mediaRetrievalDataSource?.apply {
            coverUrl = getCoverArtUrl(child.coverArt ?: "")
            streamUrl = getStreamUrl(id)
            downloadUrl = getDownloadUrl(id)
        }
        val isFavourite = child.starred != null
        return Song(
            id = id,
            name = name,
            singer = singer,
            imageUrl = coverUrl,
            streamUrl = streamUrl,
            downloadUrl = downloadUrl,
            duration = duration,
            isFavourite = isFavourite
        )
    }
}