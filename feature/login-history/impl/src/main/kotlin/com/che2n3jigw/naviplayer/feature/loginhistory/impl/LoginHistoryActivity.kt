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

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.che2n3jigw.naviplayer.core.common.BaseActivity
import com.che2n3jigw.naviplayer.feature.loginhistory.impl.databinding.ActivityLoginHistoryBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 登录历史页面
 */
@AndroidEntryPoint
class LoginHistoryActivity : BaseActivity<ActivityLoginHistoryBinding>() {

    private val viewmodel: LoginHistoryViewModel by viewModels()
    private val adapter = LoginHistoryAdapter()

    override fun inflateBinding(): ActivityLoginHistoryBinding {
        return ActivityLoginHistoryBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.rvLoginHistory.apply {
            layoutManager = LinearLayoutManager(this@LoginHistoryActivity).apply {
                orientation = RecyclerView.VERTICAL
            }
            // 添加分割线
            val decoration = MaterialDividerItemDecoration(this.context, RecyclerView.VERTICAL)
            decoration.dividerColor = Color.TRANSPARENT
            decoration.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
            addItemDecoration(decoration)
            adapter = this@LoginHistoryActivity.adapter
        }
    }

    override fun initListener() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentState = viewmodel.uiState.value
                if (currentState is LoginHistoryUiState.Success && currentState.inDeleteMode) {
                    viewmodel.toggleDeleteMode()
                } else {
                    finish()
                }
            }
        })

        binding.ibDelete.setOnClickListener {
            viewmodel.toggleDeleteMode()
        }
        binding.tvCancel.setOnClickListener {
            viewmodel.toggleDeleteMode()
        }
        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        adapter.onItemCheckedChanged = { item, isChecked ->
            viewmodel.toggleItemChecked(item, isChecked)
        }
    }

    override fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.uiState.collect {
                    when (it) {
                        is LoginHistoryUiState.Success -> {
                            adapter.submitList(it.selectableLoginHistories)
                            adapter.inDeleteMode = it.inDeleteMode
                            if (it.inDeleteMode) {
                                binding.ibDelete.visibility = View.GONE
                                binding.tvCancel.visibility = View.VISIBLE
                                binding.btnDelete.visibility = View.VISIBLE
                            } else {
                                binding.ibDelete.visibility = View.VISIBLE
                                binding.tvCancel.visibility = View.GONE
                                binding.btnDelete.visibility = View.GONE
                            }
                        }

                        is LoginHistoryUiState.Error -> {}
                        LoginHistoryUiState.Loading -> {}
                    }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.tvTitle.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }
}