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
// 创建时间： 2026/4/9 11:07
package com.che2n3jigw.naviplayer.feature.me.impl.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.che2n3jigw.naviplayer.feature.me.impl.databinding.ViewLibraryStatBinding

/**
 * 音乐库统计视图
 */
class LibraryStatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding = ViewLibraryStatBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(iconRes: Int, value: String, label: String) {
        binding.ivStatIcon.setImageResource(iconRes)
        binding.tvStatValue.text = value
        binding.tvStatLabel.text = label
    }
}