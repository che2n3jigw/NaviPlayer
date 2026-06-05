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
// 创建时间： 2026/6/5 11:05
package com.che2n3jigw.naviplayer.feature.login.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _serverFormateError = MutableSharedFlow<Unit>()
    val serverFormateError = _serverFormateError.asSharedFlow()

    private val _loading = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()

    private val _loginResult = MutableSharedFlow<Boolean>()
    val loginResult = _loginResult.asSharedFlow()

    /**
     * 登录
     */
    fun login(server: String, username: String, password: String) {
        viewModelScope.launch {
            // 检查域名
            if (!server.startsWith("http://") && !server.startsWith("https://")) {
                _serverFormateError.emit(Unit)
                return@launch
            }
            // 检查是否包含端口
            if (!server.contains(":")) {
                _serverFormateError.emit(Unit)
                return@launch
            }

            // 登录
            _loading.emit(true)
            val success = userRepository.login(server, username, password)
            _loading.emit(false)
            _loginResult.emit(success)
        }
    }
}