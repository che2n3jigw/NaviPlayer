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
package com.che2n3jigw.naviplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.che2n3jigw.naviplayer.core.database.model.LoginHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoginHistoryDao {
    @Query("SELECT * FROM login_history ORDER BY time DESC")
    fun getLoginHistory(): Flow<List<LoginHistoryEntity>>

    @Upsert
    suspend fun upsertLoginHistory(entity: LoginHistoryEntity)

    /**
     * 根据复合主键 (serverAddress, username) 删除一个特定的登录历史记录。
     */
    @Query(value = "DELETE FROM login_history WHERE serverAddress = :serverAddress AND username = :username")
    suspend fun deleteLoginHistory(serverAddress: String, username: String)
}