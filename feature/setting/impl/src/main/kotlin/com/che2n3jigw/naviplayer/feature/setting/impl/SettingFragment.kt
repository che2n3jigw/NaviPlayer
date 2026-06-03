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
// 创建时间： 2026/4/1 16:19
package com.che2n3jigw.naviplayer.feature.setting.impl

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.login.api.navigation.LoginNavigator
import com.che2n3jigw.naviplayer.feature.setting.impl.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 我的页面
 */
@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    companion object {
        private const val TAG = "SettingFragment"
    }

    @Inject
    lateinit var loginNavigator: LoginNavigator

    val viewModel: SettingViewModel by viewModels()

    override fun inflateBinding() = FragmentSettingBinding.inflate(layoutInflater)

    override fun initView() {
        binding.tvVersion.text = getString(R.string.setting_version, getVersion())
    }

    override fun initListener() {
        binding.tvIssue.setOnClickListener {
            toIssuePage()
        }
        binding.mbLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.mbLogin.setOnClickListener {
            loginNavigator.navigateToLogin(findNavController())
        }
        binding.mbTestConnection.setOnClickListener {
            viewModel.testConnection()
        }
        binding.mbClearCache.setOnClickListener {
            viewModel.clearCache()
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.mbLogout.isInvisible = !it.isLoggedIn
                    binding.mbLogin.isInvisible = it.isLoggedIn
                    binding.mcvUser.isVisible = it.isLoggedIn
                    binding.tvStorageUsage.text =
                        getString(R.string.setting_storage_usage, it.cacheSize)
                    binding.lpiUsed.setProgressCompat(it.used, true)
                    binding.tvUsername.text = it.username
                    binding.tvServer.text = it.server
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.connectionResult.collect {
                    val tip = if (it) {
                        R.string.setting_connection_success
                    } else {
                        R.string.setting_connection_failed
                    }
                    Toast.makeText(requireContext(), tip, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.viewSplit.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topMargin = insets.top
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshCacheSize()
    }

    private fun getVersion(): String {
        var versionName = getString(R.string.setting_get_version_failed)
        try {
            requireContext().apply {
                packageManager.getPackageInfo(packageName, 0).versionName?.let { versionName = it }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getVersion: ", e)
        }
        return versionName
    }

    private fun toIssuePage() {
        val url = "https://github.com/che2n3jigw/NaviPlayer/issues/new"
        val intent = Intent(Intent.ACTION_VIEW).apply { data = url.toUri() }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // 防止由于没有浏览器导致崩溃（虽然概率极低）
            Log.e(TAG, "Failed to open browser", e)
        }
    }
}