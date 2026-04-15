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

package com.che2n3jigw.naviplayer.core.data.repository

import androidx.datastore.core.DataStore
import com.che2n3jigw.naviplayer.core.data.session.SubsonicSessionManager
import com.che2n3jigw.naviplayer.core.datastore.NaviPreferencesDataSource
import com.che2n3jigw.naviplayer.core.datastore.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

// 作者: che2n3jigw
// 邮箱: che2n3jigw@163.com
// 博客: che2n3jigw.github.io
// 创建时间： 2026/4/13 18:01
class DefaultSubsonicRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subsonicRepository: DefaultSubsonicRepository

    private lateinit var subsonicSessionManager: SubsonicSessionManager

    private lateinit var naviPreferencesDataSource: NaviPreferencesDataSource

    @Before
    fun setup() {
        naviPreferencesDataSource = NaviPreferencesDataSource(object : DataStore<UserPreferences> {
            override val data
                get() = MutableStateFlow(
                    UserPreferences(
                        domain = "http://192.168.50.247:4533",
                        username = "guest",
                        password = "123456"
                    )
                )

            override suspend fun updateData(transform: suspend (t: UserPreferences) -> UserPreferences): UserPreferences {
                return data.updateAndGet { transform(it) }
            }
        })
        subsonicSessionManager = SubsonicSessionManager(naviPreferencesDataSource, testScope)
        subsonicRepository = DefaultSubsonicRepository(subsonicSessionManager)
    }

    @Test
    fun requestTest() {
        runTest {
            val artistList = subsonicRepository.getFavouriteList()
            println(artistList)
        }
    }
}