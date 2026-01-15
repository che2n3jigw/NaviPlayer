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
 *
 *
 */

// 作者: che2n3jigw
// 邮箱: che2n3jigw@163.com
// 博客: che2n3jigw.github.io
// 创建时间： 1/14/26
package com.che2n3jigw.naviplayer.feature.loginhistory.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * 登录历史数据源
 *
 * 在实际应用中，这可能是一个从数据库（例如 Room）或远程服务器获取数据的接口及其实现。
 */
class LoginHistoryDataSource {

    /**
     * 获取登录历史记录列表。
     * 这是一个模拟实现。
     *
     * @return 登录历史记录列表的 Flow
     */
    fun getLoginHistoryList(): Flow<List<LoginHistory>> {
        return flowOf(listOf(
            LoginHistory(
                serverAddress = "192.168.1.100:8080",
                username = "user1",
                password = "password1",
                success = true,
                time = "2024-07-29 10:00:00"
            ),
            LoginHistory(
                serverAddress = "10.0.2.2:8080",
                username = "user2",
                password = "password2",
                success = false,
                time = "2024-07-29 10:05:00"
            ),
            LoginHistory(
                serverAddress = "subsonic.example.com",
                username = "admin",
                password = "admin",
                success = true,
                time = "2024-07-28 15:30:00"
            )
        ))
    }
}