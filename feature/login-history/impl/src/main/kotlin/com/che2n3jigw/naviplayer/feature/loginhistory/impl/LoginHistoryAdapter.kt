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

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.che2n3jigw.naviplayer.feature.loginhistory.impl.databinding.ItemLoginHistoryBinding

/**
 * 登录历史列表适配器
 */
class LoginHistoryAdapter : BaseQuickAdapter<LoginHistory, LoginHistoryAdapter.VH>() {
    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: LoginHistory?) {
        holder.binding.tvServerAddress.text = item?.serverAddress ?: ""

        var statusRes = R.string.string_failed
        var stateColorRes = R.color.login_history_status_failed
        if (item?.success == true) {
            statusRes = R.string.string_success
            stateColorRes = R.color.login_history_status_success
        }

        holder.binding.tvStatus.setText(statusRes)
        holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, stateColorRes))
    }


    class VH(
        parent: ViewGroup, val binding: ItemLoginHistoryBinding = ItemLoginHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)
}