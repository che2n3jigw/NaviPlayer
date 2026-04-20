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
// 创建时间： 2026/4/17 16:29
package com.che2n3jigw.naviplayer.core.media

import android.app.PendingIntent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession

/**
 * 播放服务基类
 */
abstract class BasePlaybackService : MediaLibraryService() {

    abstract fun getSingleTopActivity(): PendingIntent?

    private lateinit var mediaLibrarySession: MediaLibrarySession

    private val callback = object : MediaLibrarySession.Callback {

    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaLibrarySession = MediaLibrarySession.Builder(this, player, callback).build()

        getSingleTopActivity()?.let {
            mediaLibrarySession.setSessionActivity(it)
        }
    }

    override fun onDestroy() {
        mediaLibrarySession.player.release()
        mediaLibrarySession.release()
        super.onDestroy()
    }
}