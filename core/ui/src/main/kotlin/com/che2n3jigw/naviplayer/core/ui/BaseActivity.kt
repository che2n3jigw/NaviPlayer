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
// 创建时间： 1/21/26
package com.che2n3jigw.naviplayer.core.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding

/**
 * 应用程序中所有 Activity 的基类。
 * 它封装了通用的功能，如 ViewBinding 的初始化和沉浸式窗口处理。
 *
 * @param VB 具体的 ViewBinding 类型，由子类提供。
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    /**
     * 子类可以访问的 ViewBinding 实例。
     */
    protected lateinit var binding: VB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 通过抽象方法初始化 binding，并设置内容视图
        binding = inflateBinding()
        setContentView(binding.root)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            // 手动滑出系统栏自动隐藏
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // 隐藏导航栏
            hide(WindowInsetsCompat.Type.navigationBars())
        }

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

    /**
     * 子类必须实现此方法来提供具体的 ViewBinding 实例。
     *
     * 示例用法：
     * `override fun inflateBinding() = ActivityLoginHistoryBinding.inflate(layoutInflater)`
     */
    protected abstract fun inflateBinding(): VB

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
