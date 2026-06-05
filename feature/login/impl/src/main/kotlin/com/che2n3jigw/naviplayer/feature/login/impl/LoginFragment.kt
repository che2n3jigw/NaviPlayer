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


import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.login.impl.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 登录页面
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun inflateBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun initView() {}

    override fun initListener() {
        binding.mbLogin.setOnClickListener {
            val server = binding.tilServer.editText?.text.toString()
            val username = binding.tilUsername.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()
            if (server.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showTips(R.string.login_input_empty_tip)
                return@setOnClickListener
            }
            viewModel.login(server, username, password)
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.serverFormateError.collect {
                        showTips(R.string.login_server_error)
                    }
                }

                launch {
                    viewModel.loading.collect {
                        binding.loading.isVisible = it
                        binding.mbLogin.isInvisible = it
                    }
                }
                launch {
                    viewModel.loginResult.collect {
                        if (it) {
                            findNavController().popBackStack()
                        } else {
                            showTips(R.string.login_login_error)
                        }
                    }
                }
            }
        }
    }

    private fun showTips(resId: Int) {
        Snackbar.make(binding.root, resId, Snackbar.LENGTH_SHORT).show()
    }
}