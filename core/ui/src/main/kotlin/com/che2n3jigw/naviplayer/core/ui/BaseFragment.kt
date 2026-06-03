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
// 创建时间： 2026/3/30 14:48
package com.che2n3jigw.naviplayer.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * 应用程序中所有 Fragment 的基类。
 * 它封装了通用的功能，如 ViewBinding 的初始化和沉浸式窗口处理。
 *
 * @param VB 具体的 ViewBinding 类型，由子类提供。
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    /**
     * 子类可以访问的 ViewBinding 实例。
     */
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    private var emptyView: View? = null
    private var errorView: View? = null
    private var notLoginView: View? = null


    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        _binding = inflateBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 设置 WindowInsets 监听，以处理系统栏（如状态栏和导航栏）带来的边距问题。
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // 将 insets 传递给子类，由子类决定如何调整自己的布局。
            onApplyWindowInsets(insets)
            windowInsets
        }

        // 调用模板方法，由子类实现具体的初始化逻辑
        initView()
        initListener()
        subscribeUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // 必须清空这些缓存的 View 引用，因为它们关联的是旧的视图层级
        emptyView = null
        errorView = null
        notLoginView = null
    }

    /**
     * 当窗口边距（insets）应用时调用。
     * 子类可以重写此方法来根据系统栏的高度调整其内部视图的边距或填充。
     *
     * @param insets 包含系统栏尺寸的 Insets 对象。
     */
    protected open fun onApplyWindowInsets(insets: Insets) {
        // 默认实现为空。子类可以根据需要重写。
        // 例如：binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        //           topMargin = insets.top
        //       }
    }


    /**
     * 子类调用此方法订阅状态流
     */
    protected fun observePageUiState(flow: Flow<PageUiState>) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect {
                    // 内容显示
                    val showContent =
                        it !is PageUiState.Loading && it !is PageUiState.Empty && it !is PageUiState.Error
                    getContentView()?.forEach { content ->
                        content.isVisible = showContent
                    }

                    // 加载框显示
                    getLoadingView()?.isVisible = it is PageUiState.Loading

                    // 空数据显示
                    emptyView = emptyView ?: getEmptyView()?.inflate()
                    emptyView?.isVisible = it is PageUiState.Empty

                    // 加载异常显示
                    errorView = errorView ?: getErrorView()?.inflate()
                    errorView?.isVisible = it is PageUiState.Error

                    // 未登录显示
                    notLoginView = notLoginView ?: getNotLoginView()?.inflate()
                    notLoginView?.isVisible = it is PageUiState.NotLogin
                }
            }
        }
    }

    protected open fun getContentView(): List<View>? = null
    protected open fun getLoadingView(): View? = null
    protected open fun getEmptyView(): ViewStub? = null
    protected open fun getErrorView(): ViewStub? = null
    protected open fun getNotLoginView(): ViewStub? = null

    /**
     * 子类必须实现此方法来提供具体的 ViewBinding 实例。
     *
     * 示例用法：
     * `override fun inflateBinding() = XXXBinding.inflate(layoutInflater)`
     */
    protected abstract fun inflateBinding(): VB

    /**
     * 初始化视图。例如，设置 RecyclerView 的适配器。
     */
    protected abstract fun initView()

    /**
     * 初始化事件监听器。
     */
    protected abstract fun initListener()

    /**
     * 订阅 UI 状态的更新。
     */
    protected abstract fun subscribeUI()
}
