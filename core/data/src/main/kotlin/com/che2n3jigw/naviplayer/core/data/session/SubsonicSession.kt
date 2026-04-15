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
// 创建时间： 2026/4/7 16:49
package com.che2n3jigw.naviplayer.core.data.session

import com.che2n3jigw.android.libs.opensubsonicapi.datasource.BrowsingDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.ListsDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.MediaRetrievalDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.PlaylistsDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.SystemDataSource

/**
 * 代表一个活跃的登录会话
 */
data class SubsonicSession(
    // 将所有常用的 DataSource 组合在一起
    val systemDataSource: SystemDataSource,
    val browsingDataSource: BrowsingDataSource,
    val listsDataSource: ListsDataSource,
    val mediaRetrievalDataSource: MediaRetrievalDataSource,
    val playlistsDataSource: PlaylistsDataSource
)