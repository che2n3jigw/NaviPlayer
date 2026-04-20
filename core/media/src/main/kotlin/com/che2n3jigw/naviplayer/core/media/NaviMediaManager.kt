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
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.che2n3jigw.naviplayer.core.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 媒体播放统一管理类 (单例)
 * 负责维护全应用唯一的 MediaBrowser 连接
 */
@Singleton
class NaviMediaManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    sessionToken: SessionToken
) {

    companion object {
        private const val TAG = "NaviMediaManager"
    }

    private var browserFuture = MediaBrowser.Builder(context, sessionToken).buildAsync()

    /**
     * 获取当前的 MediaBrowser 实例
     */
    val browser: MediaBrowser?
        get() = if (browserFuture.isDone && !browserFuture.isCancelled) {
            try {
                browserFuture.get()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get browser", e)
                null
            }
        } else null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist = _playlist.asStateFlow()

    private val songCache = mutableMapOf<String, Song>()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    init {
        browserFuture.addListener({
            try {
                val currentBrowser = browserFuture.get()
                // 1. 设置初始状态
                _isPlaying.value = currentBrowser.isPlaying
                // 2. 监听后续变化
                currentBrowser.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _isPlaying.value = isPlaying
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        _currentSong.value = songCache[mediaItem?.mediaId]
                    }
                })
            } catch (e: Exception) {
                Log.e(TAG, "MediaBrowser connection failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
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
        browser?.setMediaItems(mediaItems)
        browser?.prepare()
    }

    /**
     * 播放/暂停 切换逻辑
     */
    fun togglePlay() {
        browser?.let {
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

    /**
     * 释放资源 (可选：通常随应用进程销毁，如需手动注销可调用)
     */
    fun release() {
        if (!browserFuture.isDone) {
            browserFuture.cancel(true)
        }
        MediaBrowser.releaseFuture(browserFuture)
    }
}
