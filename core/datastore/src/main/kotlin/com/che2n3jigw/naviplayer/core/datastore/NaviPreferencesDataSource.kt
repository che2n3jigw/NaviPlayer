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
// 创建时间： 2026/4/7 14:09
package com.che2n3jigw.naviplayer.core.datastore

import UserData
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 使用Jetpack DataStore管理用户偏好和会话状态的数据源
 */
class NaviPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map {
        UserData(
            domain = it.domain,
            username = it.username,
            password = it.password,
            isLoggedIn = it.domain.isNotBlank() && it.username.isNotBlank() && it.password.isNotBlank(),
            currentSongId = it.currentSongId
        )
    }

    suspend fun login(domain: String, username: String, password: String) {
        userPreferences.updateData {
            it.copy(
                domain = domain,
                username = username,
                password = password
            )
        }
    }

    suspend fun logout() {
        userPreferences.updateData {
            it.copy(
                domain = "",
                username = "",
                password = "",
                currentSongId = ""
            )
        }
    }
}