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
// 创建时间： 2026/5/21 13:58
package com.che2n3jigw.naviplayer.feature.player.impl

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.che2n3jigw.naviplayer.core.navigation.CommonNavOptions
import com.che2n3jigw.naviplayer.feature.playlist.api.PlaylistDeepLink
import com.che2n3jigw.naviplayer.feature.playlist.api.PlaylistNavigator
import javax.inject.Inject

class PlaylistNavigatorImpl @Inject constructor() : PlaylistNavigator {
    override fun navigateToPlaylist(navController: NavController) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(PlaylistDeepLink.PLAYLIST.toUri())
            .build()
        navController.navigate(request, CommonNavOptions.slideInOptions)
    }

    override fun navigateToPlaylists(navController: NavController) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(PlaylistDeepLink.PLAYLISTS.toUri())
            .build()
        navController.navigate(request, CommonNavOptions.slideInOptions)
    }
}