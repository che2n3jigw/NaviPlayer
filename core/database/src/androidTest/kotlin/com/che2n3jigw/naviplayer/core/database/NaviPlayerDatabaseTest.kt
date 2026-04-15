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

package com.che2n3jigw.naviplayer.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.che2n3jigw.naviplayer.core.database.dao.PlayHistoryDao
import org.junit.After
import org.junit.Before

// 作者: che2n3jigw
// 邮箱: che2n3jigw@163.com
// 博客: che2n3jigw.github.io
// 创建时间： 2026/4/15 17:59

internal abstract class NaviPlayerDatabaseTest {

    private lateinit var db: NaviPlayerDatabase
    protected lateinit var playHistoryDao: PlayHistoryDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                NaviPlayerDatabase::class.java
            ).build()
        }
        playHistoryDao = db.playHistoryDao()
    }

    @After
    fun tearDown() = db.close()
}