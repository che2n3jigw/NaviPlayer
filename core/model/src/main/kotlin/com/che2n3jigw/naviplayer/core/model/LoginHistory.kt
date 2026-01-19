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
package com.che2n3jigw.naviplayer.core.model

/**
 * 登录历史
 */
data class LoginHistory(
    /**
     * 服务器地质
     */
    val server: String,
    /**
     * 账号
     */
    val username: String,
    /**
     * 密码
     */
    val password: String,
    /**
     * 登陆状态
     */
    val success: Boolean,
    /**
     * 时间
     * 格式:2023-01-02T23:40:57.120Z
     */
    val time: String
)