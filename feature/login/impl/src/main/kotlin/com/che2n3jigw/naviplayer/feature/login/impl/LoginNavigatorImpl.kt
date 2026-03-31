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
// 创建时间： 2026/3/31 14:33
package com.che2n3jigw.naviplayer.feature.login.impl

import androidx.navigation.NavController
import com.che2n3jigw.naviplayer.core.navigation.CommonNavOptions
import com.che2n3jigw.naviplayer.feature.login.api.navigation.LoginNavigator
import javax.inject.Inject

/**
 * 登录导航实现类
 */
class LoginNavigatorImpl @Inject constructor() : LoginNavigator {

    override fun navigateToLogin(navController: NavController) {
        navController.navigate(R.id.login_dest, CommonNavOptions.slideInOptions)
    }
}