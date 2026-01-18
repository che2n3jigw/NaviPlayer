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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultLoginHistoryRepository @Inject constructor(
    private val loginHistoryDao: LoginHistoryDao
) : LoginHistoryRepository {

    override fun getLoginHistory(): Flow<List<LoginHistoryEntity>> {
        return loginHistoryDao.getLoginHistory()
    }

    override suspend fun upsertLoginHistory(entity: LoginHistoryEntity) {
        return withContext(Dispatchers.IO) {
            loginHistoryDao.upsertLoginHistory(entity)
        }
    }

    override suspend fun deleteLoginHistory(addresses: List<String>) {
        return withContext(Dispatchers.IO) {
            loginHistoryDao.deleteLoginHistory(addresses)
        }
    }
}