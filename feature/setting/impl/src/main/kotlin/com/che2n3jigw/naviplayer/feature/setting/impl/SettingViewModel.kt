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
// 创建时间： 2026/4/21 16:52
package com.che2n3jigw.naviplayer.feature.setting.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ln
import kotlin.math.pow

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val subsonicRepository: SubsonicRepository
) : ViewModel() {

    private val _cacheSize = MutableStateFlow(0L)

    private val _connectionResult = MutableSharedFlow<Boolean>()
    val connectionResult = _connectionResult.asSharedFlow()

    val uiState = combine(
        userRepository.userData,
        subsonicRepository.activeSession,
        _cacheSize
    ) { userData, _, cacheSize ->
        val used = cacheSize.toFloat() / (2 * 1024 * 1024 * 8) * 100
        SettingUiState(
            isLoggedIn = userData.isLoggedIn,
            username = userData.username,
            avatar = subsonicRepository.getAvatarUrl(userData.username),
            server = userData.domain,
            cacheSize = toReadableFileSize(cacheSize),
            used = used.toInt()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingUiState()
    )

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    private fun toReadableFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (ln(size.toDouble()) / ln(1024.0)).toInt()
        val value = size / 1024.0.pow(digitGroups.toDouble())
        return if (digitGroups == 0) {
            "$size B"
        } else {
            String.format(Locale.US, "%.1f %s", value, units[digitGroups])
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _connectionResult.tryEmit(userRepository.ping())
        }
    }
}

data class SettingUiState(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val avatar: String = "",
    val server: String = "",
    val cacheSize: String = "",
    val used: Int = 0
)