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
// 创建时间： 2026/3/30 14:36
package com.che2n3jigw.naviplayer.feature.login.impl


import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.che2n3jigw.naviplayer.core.data.repository.UserRepository
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.login.impl.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 登录页面
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun inflateBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun initView() {}

    override fun initListener() {
        binding.btnFakeLogin.setOnClickListener {
            lifecycleScope.launch {
                val success = userRepository.login("http://192.168.50.247:4533", "guest", "123456")
                if (success) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun subscribeUI() {}
}