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
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.che2n3jigw.naviplayer.core.common.ApplicationScope
import com.che2n3jigw.naviplayer.core.data.repository.UserPlaybackRepository
import com.che2n3jigw.naviplayer.core.media.api.PlayerController
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 媒体播放统一管理类 (单例)
 * 负责维护全应用唯一的 MediaBrowser 连接
 */
@Singleton
class NaviMediaManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:ApplicationScope private val scope: CoroutineScope,
    private val sessionToken: SessionToken,
    private val userPlaybackRepository: UserPlaybackRepository
) : PlayerController {

    // <editor-fold defaultState="collapsed" desc="Flow">
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist = _playlist.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()
    // </editor-fold>

    // <editor-fold defaultState="collapsed" desc="播放器相关">
    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private lateinit var browser: MediaController
    private val playListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            // 更新播放状态
            _isPlaying.update { isPlaying }
            // 根据播放状态开启/关闭检查播放进度任务
            if (!isPlaying) {
                checkPlaybackPositionJob?.cancel()
            } else {
                checkPlaybackPosition()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            // 获取当前播放歌曲并记录
            scope.launch {
                _playlist.first().find { it.id == mediaItem?.mediaId }?.let { song ->
                    _currentSong.update { song }
                    userPlaybackRepository.upsertPlayback(song)
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultState="collapsed" desc="播放进度相关">
    private var checkPlaybackPositionJob: Job? = null
    private val _position = MutableStateFlow(0L)
    val position = _position.asStateFlow()
    // </editor-fold>

    /**
     * 初始化媒体管理器
     */
    fun initialize() {
        scope.launch {
            withContext(Dispatchers.Main) {
                browserFuture = MediaBrowser.Builder(context, sessionToken).buildAsync()
                browser = browserFuture.await()
                _isPlaying.update { browser.isPlaying }
                browser.addListener(playListener)
            }
        }
    }

    /**
     * 释放资源 (可选：通常随应用进程销毁，如需手动注销可调用)
     */
    fun release() {
        browser.removeListener(playListener)
        MediaBrowser.releaseFuture(browserFuture)
        checkPlaybackPositionJob?.cancel()
    }

    /**
     * 仅更新内存中歌曲的状态（如收藏属性），而不触碰播放器实例
     */
    fun updateSongMetadata(songId: String, isFavourite: Boolean) {
        // 1. 更新播放列表中的对应歌曲
        _playlist.update { currentList ->
            currentList.map {
                if (it.id == songId) it.copy(isFavourite = isFavourite) else it
            }
        }

        // 2. 如果是当前播放的歌曲，也需要更新
        if (_currentSong.value?.id == songId) {
            _currentSong.update { it?.copy(isFavourite = isFavourite) }
        }
    }

    private fun checkPlaybackPosition() {
        checkPlaybackPositionJob = CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            while (isActive) {
                _position.update { browser.currentPosition }
                delay(1000)
            }
        }
    }

    // <editor-fold defaultState="collapsed" desc="PlaybackController">
    override fun play() {
        scope.launch {
            withContext(Dispatchers.Main) {
                if (!browser.isPlaying) {
                    if (browser.playbackState == Player.STATE_IDLE) {
                        browser.prepare()
                    }
                    browser.play()
                }
            }
        }
    }

    override fun pause() {
        scope.launch {
            withContext(Dispatchers.Main) {
                if (browser.isPlaying) {
                    browser.pause()
                }
            }
        }
    }

    override fun togglePlayPause() {
        scope.launch {
            withContext(Dispatchers.Main) {
                if (browser.isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
        }
    }

    override fun skipToNext() {
        scope.launch {
            withContext(Dispatchers.Main) {
                if (browser.hasNextMediaItem()) {
                    browser.seekToNextMediaItem()
                }
            }
        }
    }

    override fun skipToPrevious() {
        scope.launch {
            withContext(Dispatchers.Main) {
                if (browser.hasPreviousMediaItem()) {
                    browser.seekToPreviousMediaItem()
                }
            }
        }
    }

    override fun seekTo(positionMs: Long) {
        scope.launch {
            withContext(Dispatchers.Main) {
                browser.seekTo(positionMs)
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultState="collapsed" desc="QueueController">
    override fun play(songs: List<Song>, index: Int) {
        scope.launch {
            // 更新播放列表
            _playlist.update { songs }
            // song转换mediaItem
            val mediaItems = songs.map {
                MediaItem.Builder().setUri(it.streamUrl).setMediaId(it.id).build()
            }
            // 将mediaItem给到媒体播放器
            withContext(Dispatchers.Main) {
                browser.setMediaItems(mediaItems, index, C.TIME_UNSET)
                browser.prepare()
                browser.play()
            }
        }
    }

    override fun skipToItem(index: Int) {
        scope.launch {
            withContext(Dispatchers.Main) {
                browser.seekTo(index, C.TIME_UNSET)
            }
        }
    }
    // </editor-fold>
}
