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
// 创建时间： 2026/4/15 17:25
package com.che2n3jigw.naviplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.che2n3jigw.naviplayer.core.database.model.PlayListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Query("SELECT * FROM play_list")
    fun getPlayList(): Flow<List<PlayListEntity>>

    /**
     * 删除表中所有旧记录
     */
    @Query("DELETE FROM play_list")
    suspend fun clearPlayList()

    /**
     * 插入新记录列表
     */
    @Insert
    suspend fun insertPlayList(playList: List<PlayListEntity>)

    /**
     * 覆盖播放列表（事务操作：先清空，后插入）
     */
    @Transaction
    suspend fun overwritePlayList(playList: List<PlayListEntity>) {
        clearPlayList()
        insertPlayList(playList)
    }
}