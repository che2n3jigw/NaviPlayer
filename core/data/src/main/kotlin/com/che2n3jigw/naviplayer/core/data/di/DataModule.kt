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
package com.che2n3jigw.naviplayer.core.data.di

import com.che2n3jigw.naviplayer.core.data.repository.DefaultLoginHistoryRepository
import com.che2n3jigw.naviplayer.core.data.repository.DefaultSubsonicRepository
import com.che2n3jigw.naviplayer.core.data.repository.DefaultUserRepository
import com.che2n3jigw.naviplayer.core.data.repository.LoginHistoryRepository
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsLoginHistoryRepository(
        defaultLoginHistoryRepository: DefaultLoginHistoryRepository
    ): LoginHistoryRepository

    @Binds
    internal abstract fun bindsUserRepository(
        defaultUserRepository: DefaultUserRepository
    ): UserRepository

    @Binds
    internal abstract fun bindSubsonicRepository(
        defaultSubsonicRepository: DefaultSubsonicRepository
    ): SubsonicRepository
}