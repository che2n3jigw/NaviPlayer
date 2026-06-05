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
// 创建时间： 2026/4/7 15:42
package com.che2n3jigw.naviplayer.core.data.repository

import UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    /**
     * 实时观察当前用户信息和登录状态
     */
    val userData: Flow<UserData>

    /**
     * 服务器实时连通性状态
     */
    val isServerReachable: StateFlow<Boolean>

    /**
     * 尝试登录服务器
     */
    suspend fun login(domain: String, username: String, password: String): Boolean

    /**
     * 退出登录，清除本地持久化的用户信息
     */
    suspend fun logout()

    /**
     * 检查当前配置的服务器连通性
     * 适用于：在不重新登录的情况下，检查服务器是否在线
     */
    suspend fun ping(): Boolean

    /**
     * 更新当前播放歌曲的id
     */
    suspend fun updateCurrentSongId(songId: String)
}