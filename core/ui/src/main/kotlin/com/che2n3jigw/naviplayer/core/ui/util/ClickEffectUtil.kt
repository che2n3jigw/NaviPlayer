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

package com.che2n3jigw.naviplayer.core.ui.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

/**
 * 视图点击动效工具类
 */
object ClickEffectUtil {
    private const val DEFAULT_DURATION = 200L
    private const val DEFAULT_PRESSED_SCALE = 0.95f
    private const val DEFAULT_ENLARGE_SCALE = 1.1f

    /**
     * 常用：按下缩小效果
     * @param view 触发点击的视图
     * @param targetView 实际响应动画的视图（默认为触发视图本身）
     */
    fun applyScale(view: View, targetView: View = view) {
        applyEffect(view, targetView, DEFAULT_PRESSED_SCALE)
    }

    /**
     * 常用：按下放大效果
     * @param view 触发点击的视图
     * @param targetView 实际响应动画的视图（默认为触发视图本身）
     */
    fun applyEnlarge(view: View, targetView: View = view) {
        applyEffect(view, targetView, DEFAULT_ENLARGE_SCALE)
    }

    /**
     * 通用点击动效逻辑
     */
    @SuppressLint("ClickableViewAccessibility")
    fun applyEffect(
        view: View,
        targetView: View = view,
        scale: Float,
        duration: Long = DEFAULT_DURATION
    ) {
        val touchSlop = ViewConfiguration.get(view.context).scaledTouchSlop
        var downX = 0f
        var downY = 0f
        var isClick = true

        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    isClick = true
                    targetView.animate()
                        .scaleX(scale)
                        .scaleY(scale)
                        .setDuration(duration)
                        .start()
                    // 必须返回 true 才能收到后续事件，但不影响 onClick
                    view.isPressed = true
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isClick) {
                        val dx = abs(event.x - downX)
                        val dy = abs(event.y - downY)
                        if (dx > touchSlop || dy > touchSlop) {
                            isClick = false
                            view.isPressed = false
                            targetView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(duration)
                                .start()
                        }
                    }
                    false
                }

                MotionEvent.ACTION_UP -> {
                    view.isPressed = false
                    targetView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(duration)
                        .start()
                    if (isClick) {
                        view.performClick()
                    }
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    view.isPressed = false
                    targetView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(duration)
                        .start()
                    true
                }

                else -> false
            }
        }
    }
}