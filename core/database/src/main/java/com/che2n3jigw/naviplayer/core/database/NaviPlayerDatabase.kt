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
package com.che2n3jigw.naviplayer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.che2n3jigw.naviplayer.core.database.dao.LoginHistoryDao
import com.che2n3jigw.naviplayer.core.database.dao.PlayHistoryDao
import com.che2n3jigw.naviplayer.core.database.model.LoginHistoryEntity
import com.che2n3jigw.naviplayer.core.database.model.PlayHistoryEntity
import com.che2n3jigw.naviplayer.core.database.util.InstantConverter

@Database(
    entities = [LoginHistoryEntity::class, PlayHistoryEntity::class],
    version = 2,
    autoMigrations = [],
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
internal abstract class NaviPlayerDatabase : RoomDatabase() {
    abstract fun loginHistoryDao(): LoginHistoryDao
    abstract fun playHistoryDao(): PlayHistoryDao
}