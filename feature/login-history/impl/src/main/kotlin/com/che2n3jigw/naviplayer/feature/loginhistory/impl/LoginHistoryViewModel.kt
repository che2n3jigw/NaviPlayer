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
// 创建时间： 1/13/26
package com.che2n3jigw.naviplayer.feature.loginhistory.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.LoginHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginHistoryViewModel @Inject constructor(
    loginHistoryRepository: LoginHistoryRepository
) : ViewModel() {

    // 私有的、可变的StateFlow，用于跟踪删除模式的状态
    private val _inDeleteMode = MutableStateFlow(false)

    //
    private val _checkedItemIds = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<LoginHistoryUiState> = combine(
        // 从数据仓库获取登录历史列表的Flow
        loginHistoryRepository.getLoginHistory(),
        // 获取表示“是否处于删除模式”的Flow
        _inDeleteMode,
        // 获取表示“已选择的项ID”的Flow
        _checkedItemIds
    ) { history, inDeleteMode, checkedItemIds ->
        // 3. 合并上方数据
        val selectableHistory = history.map { loginHistory ->
            SelectableLoginHistory(
                loginHistory = loginHistory,
                isChecked = "${loginHistory.server}${loginHistory.username}" in checkedItemIds
            )
        }
        LoginHistoryUiState.Success(selectableHistory, inDeleteMode)
    }
        // 4. 将Flow的类型从Flow<Success>向上转型为Flow<LoginHistoryUiState>
        //    这是为了让后续的onStart和catch可以发出不同类型的UiState（如Loading和Error）
        .map<LoginHistoryUiState.Success, LoginHistoryUiState> { it }
        // 5. 在Flow开始收集之前，首先发出Loading状态，通知UI显示加载指示器
        .onStart { emit(LoginHistoryUiState.Loading) }
        // 6. 捕获上游Flow（如数据库操作）中可能发生的任何异常
        .catch {
            // 如果发生异常，则发出Error状态，并将异常信息传递给UI
            emit(LoginHistoryUiState.Error(it))
        }
        // 7. 将这个冷Flow转换为热的StateFlow，以便UI可以安全地订阅
        .stateIn(
            // StateFlow的生命周期与ViewModel的生命周期绑定
            scope = viewModelScope,
            // 配置共享策略：当有订阅者时启动，当最后一个订阅者消失5秒后停止上游Flow。
            // 这可以有效避免在屏幕旋转等短暂的配置更改期间重启Flow。
            started = SharingStarted.WhileSubscribed(5_000),
            // StateFlow的初始值，在Flow开始收集前，UI会首先收到这个值。
            initialValue = LoginHistoryUiState.Loading
        )

    /**
     * 切换删除模式的开关状态
     */
    fun toggleDeleteMode() {
        _inDeleteMode.update { !it }
    }

    /**
     * 更新item的选中状态
     */
    fun toggleItemChecked(item: SelectableLoginHistory, checked: Boolean) {
        _checkedItemIds.update { currentIds ->
            val newIds = currentIds.toMutableSet()
            val id = "${item.loginHistory.server}${item.loginHistory.username}"
            if (checked) {
                newIds.add(id)
            } else {
                newIds.remove(id)
            }
            newIds
        }
    }
}

/**
 * 表示登录历史页面的不同UI状态。
 */
sealed interface LoginHistoryUiState {
    /**
     * 正在加载历史记录。
     */
    data object Loading : LoginHistoryUiState

    /**
     * 成功加载历史记录。
     * @param selectableLoginHistories       可选历史记录。
     * @param inDeleteMode  是否处于删除模式。
     */
    data class Success(
        val selectableLoginHistories: List<SelectableLoginHistory> = emptyList(),
        val inDeleteMode: Boolean = false
    ) : LoginHistoryUiState

    /**
     * 加载历史记录时发生错误。
     * @param throwable 发生的异常。
     */
    data class Error(val throwable: Throwable) : LoginHistoryUiState
}
