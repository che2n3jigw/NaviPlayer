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
// 创建时间： 1/18/26
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.naviplayer.core.database.dao.LoginHistoryDao
import com.che2n3jigw.naviplayer.core.database.model.LoginHistoryEntity
import com.che2n3jigw.naviplayer.core.database.model.asExternalModel
import com.che2n3jigw.naviplayer.core.model.LoginHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Clock

internal class DefaultLoginHistoryRepository @Inject constructor(
    private val loginHistoryDao: LoginHistoryDao
) : LoginHistoryRepository {

    override fun getLoginHistory(): Flow<List<LoginHistory>> {
        return loginHistoryDao.getLoginHistory().map { loginHistoryEntities ->
            loginHistoryEntities.map { it.asExternalModel() }
        }
    }

    override suspend fun upsertLoginHistory(
        serverAddress: String,
        username: String,
        password: String,
        success: Boolean
    ) {
        return withContext(Dispatchers.IO) {
            loginHistoryDao.upsertLoginHistory(
                LoginHistoryEntity(
                    serverAddress = serverAddress,
                    username = username,
                    password = password,
                    success = success,
                    time = Clock.System.now()
                )
            )
        }
    }

    override suspend fun deleteLoginHistory(serverAddress: String, username: String) {
        return withContext(Dispatchers.IO) {
            loginHistoryDao.deleteLoginHistory(serverAddress, username)
        }
    }
}