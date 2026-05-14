/*
 Copyright (c) 2026 che2n3jigw.
 *
 Licensed under the MIT License (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 *
 https://opensource.org/licenses/MIT
 *
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

// 作者: che2n3jigw
// 邮箱: che2n3jigw@163.com
// 博客: che2n3jigw.github.io
// 创建时间： 2026/4/20 9:10
package com.che2n3jigw.naviplayer.core.media

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.che2n3jigw.naviplayer.core.model.Song
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 媒体播放统一管理类 (单例)
 * 负责维护全应用唯一的 MediaBrowser 连接
 */
@Singleton
class NaviMediaManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val sessionToken: SessionToken
) {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist = _playlist.asStateFlow()

    private val songCache = mutableMapOf<String, Song>()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private lateinit var browser: MediaController

    // <editor-fold defaultState="collapsed" desc="播放进度相关">
    private var checkPlaybackPositionJob: Job? = null
    private val _position = MutableStateFlow(0L)
    val position = _position.asStateFlow()
    // </editor-fold>


    suspend fun initialize() {
        browserFuture = MediaBrowser.Builder(context, sessionToken).buildAsync()
        browser = browserFuture.await()
        _isPlaying.value = browser.isPlaying
        browser.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.update { isPlaying }
                if (isPlaying) {
                    checkPlaybackPositionJob?.cancel()
                } else {
                    checkPlaybackPosition()
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentSong.update { songCache[mediaItem?.mediaId] }
            }
        })
    }

    /**
     * 设置播放列表
     */
    fun setMediaItems(songs: List<Song>) {
        _playlist.value = songs
        songCache.clear()
        songs.forEach { songCache[it.id] = it }
        val mediaItems = songs.map {
            MediaItem.Builder().setUri(it.streamUrl).setMediaId(it.id).build()
        }
        browser.setMediaItems(mediaItems)
        browser.prepare()
    }

    /**
     * 播放/暂停 切换逻辑
     */
    fun togglePlay() {
        browser.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                if (it.playbackState == Player.STATE_IDLE) {
                    it.prepare()
                }
                it.play()
            }
        }
    }

    fun playNext() {
        if (browser.hasNextMediaItem()) {
            browser.seekToNextMediaItem()
        }
    }

    fun playPrevious() {
        if (browser.hasPreviousMediaItem()) {
            browser.seekToPreviousMediaItem()
        }
    }

    /**
     * 释放资源 (可选：通常随应用进程销毁，如需手动注销可调用)
     */
    fun release() {
        MediaBrowser.releaseFuture(browserFuture)
        checkPlaybackPositionJob?.cancel()
    }

    private fun checkPlaybackPosition() {
        checkPlaybackPositionJob = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            while (isActive) {
                _position.update { browser.currentPosition }
                delay(1000)
            }
        }
    }
}
