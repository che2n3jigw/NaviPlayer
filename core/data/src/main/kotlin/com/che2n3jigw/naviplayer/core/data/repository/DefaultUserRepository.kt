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
// 创建时间： 2026/4/7 16:02
package com.che2n3jigw.naviplayer.core.data.repository

import com.che2n3jigw.android.libs.opensubsonicapi.bean.AuthInfo
import com.che2n3jigw.android.libs.opensubsonicapi.datasource.SystemDataSource
import com.che2n3jigw.naviplayer.core.data.session.SubsonicSessionManager
import com.che2n3jigw.naviplayer.core.datastore.NaviPreferencesDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * [UserRepository] 的默认实现类。
 *
 * 该类负责统筹用户数据的持久化存储与活跃网络会话的管理。
 * 它充当了业务逻辑层（ViewModel）与底层数据源（DataStore 和网络 API）之间的中转站。

 * @property naviPreferencesDataSource 用户偏好的持久化数据源（基于 DataStore）。
 * @property subsonicSessionManager 全局会话管理器，负责维护基于当前账号的网络数据源实例。
 */
class DefaultUserRepository @Inject constructor(
    private val naviPreferencesDataSource: NaviPreferencesDataSource,
    private val subsonicSessionManager: SubsonicSessionManager,
    private val playListRepository: PlayListRepository
) : UserRepository {

    /**
     * 观察当前持久化的用户信息。
     */
    override val userData = naviPreferencesDataSource.userData

    /**
     * 观察当前服务器是否可达。
     */
    private val _isServerReachable = MutableStateFlow(false)
    override val isServerReachable = _isServerReachable.asStateFlow()

    /**
     * 执行登录流程。
     *
     * 该方法会首先创建临时的 [SystemDataSource] 进行 License 校验。
     * 校验成功后，会将凭证保存到磁盘，这会触发 [SubsonicSessionManager] 的响应式链条，
     * 从而自动更新全应用的活跃会话。
     *
     * @param domain 服务器地址/域名
     * @param username 用户名
     * @param password 密码
     */
    override suspend fun login(domain: String, username: String, password: String): Boolean {
        val authInfo = AuthInfo(username, password)
        val systemDataSource = SystemDataSource(domain, authInfo)
        val license = systemDataSource.getLicense()
        if (license != null) {
            // 登录成功：写入持久化存储，触发全应用 Session 刷新
            naviPreferencesDataSource.login(domain, username, password)
            _isServerReachable.update { true }
        } else {
            // 登录失败
            _isServerReachable.update { false }
        }
        return license != null
    }

    /**
     * 执行登出流程。
     *
     * 清除本地持久化的用户凭证。
     * 会自动关闭并释放当前的活跃会话资源。
     */
    override suspend fun logout() {
        // 更新 UI 状态
        _isServerReachable.update { false }
        // 清除持久化数据，触发响应式注销
        naviPreferencesDataSource.logout()
        playListRepository.clearPlayList()
    }

    /**
     * 检测当前服务器的连接状况。
     *
     * 尝试使用当前活跃会话中的 [SystemDataSource] 向服务器发起 Ping 请求。
     *
     * @return 如果服务器响应成功且 License 有效则返回 true，否则返回 false。
     */
    override suspend fun ping(): Boolean {
        val systemDataSource = subsonicSessionManager.activeSession.value?.systemDataSource
        val success = systemDataSource?.ping() ?: false
        _isServerReachable.update { success }
        return success
    }

    override suspend fun updateCurrentSongId(songId: String) {
        naviPreferencesDataSource.updateCurrentSongId(songId)
    }
}