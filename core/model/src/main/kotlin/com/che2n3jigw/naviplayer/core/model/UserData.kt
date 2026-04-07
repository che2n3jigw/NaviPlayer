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
// 创建时间： 2026/4/7 14:45
/**
 * 业务层使用的用户信息模型
 * 适配 Subsonic 协议需求
 */
data class UserData(
    /**
     * 服务器地址 (例如: http://192.168.1.1:4040)
     */
    val domain: String = "",
    /**
     * 用户名
     */
    val username: String = "",
    /**
     * 密码 (Subsonic 认证所需)
     */
    val password: String = "",
    /**
     * 是否已成功登录并连接
     */
    val isLoggedIn: Boolean = false
) {

    /**
     * 重写 toString 以屏蔽密码字段。
     * 这样在打印日志时，密码会显示为 ***，防止隐私泄露。
     */
    override fun toString(): String {
        return "UserData(domain='$domain', username='$username', isLoggedIn=$isLoggedIn, password=[***])"
    }
}