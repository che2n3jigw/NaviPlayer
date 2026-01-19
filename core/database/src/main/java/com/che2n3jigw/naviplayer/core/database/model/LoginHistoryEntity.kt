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
// 创建时间： 1/16/26
package com.che2n3jigw.naviplayer.core.database.model

import androidx.room.Entity
import com.che2n3jigw.naviplayer.core.model.LoginHistory
import kotlin.time.Instant

/**
 * 登录历史实体类
 */
@Entity(
    tableName = "login_history",
    primaryKeys = ["serverAddress", "username"]
)
data class LoginHistoryEntity(
    val serverAddress: String,
    val username: String,
    val password: String,
    val success: Boolean,
    val time: Instant
)

fun LoginHistoryEntity.asExternalModel() = LoginHistory(
    server = serverAddress,
    username = username,
    password = password,
    success = success,
    time = time.toString()
)
