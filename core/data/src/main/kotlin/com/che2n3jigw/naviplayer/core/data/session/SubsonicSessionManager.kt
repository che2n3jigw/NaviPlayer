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


import com.che2n3jigw.android.libs.opensubsonicapi.bean.AuthInfo
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.BrowsingDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.ListsDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.MediaAnnotationDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.MediaRetrievalDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.PlaylistsDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.SearchingDataSource
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.SystemDataSource
import com.che2n3jigw.naviplayer.core.common.ApplicationScope
import com.che2n3jigw.naviplayer.core.datastore.NaviPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubsonicSessionManager @Inject constructor(
    naviPreferencesDataSource: NaviPreferencesDataSource,
    @ApplicationScope scope: CoroutineScope
) {
    // 使用 scan 算子，它可以让你拿到 "oldSession"
    val activeSession: StateFlow<SubsonicSession?> = naviPreferencesDataSource.userData
        .scan(null as SubsonicSession?) { oldSession, it ->
            // 1. 在这里安全地释放旧 Session 资源
            oldSession?.systemDataSource?.close()
            oldSession?.browsingDataSource?.close()

            // 2. 根据新数据创建新 Session
            if (it.isLoggedIn) {
                val authInfo = AuthInfo(it.username, it.password)
                SubsonicSession(
                    browsingDataSource = BrowsingDataSource(it.domain, authInfo),
                    systemDataSource = SystemDataSource(it.domain, authInfo),
                    listsDataSource = ListsDataSource(it.domain, authInfo),
                    mediaRetrievalDataSource = MediaRetrievalDataSource(it.domain, authInfo),
                    playlistsDataSource = PlaylistsDataSource(it.domain, authInfo),
                    searchingDataSource = SearchingDataSource(it.domain, authInfo),
                    mediaAnnotationDataSource = MediaAnnotationDataSource(it.domain, authInfo),
                )
            } else {
                null
            }
        }
        .stateIn(scope, SharingStarted.Eagerly, null)

    val browsingDataSource get() = activeSession.value?.browsingDataSource
    val listsDataSource get() = activeSession.value?.listsDataSource
    val mediaRetrievalDataSource get() = activeSession.value?.mediaRetrievalDataSource
    val playlistsDataSource get() = activeSession.value?.playlistsDataSource
    val searchingDataSource get() = activeSession.value?.searchingDataSource
    val mediaAnnotationDataSource get() = activeSession.value?.mediaAnnotationDataSource
}