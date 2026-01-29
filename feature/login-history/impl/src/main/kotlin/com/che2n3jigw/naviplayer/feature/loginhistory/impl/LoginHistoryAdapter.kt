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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.che2n3jigw.naviplayer.feature.loginhistory.impl.LoginHistoryAdapter.VH
import com.che2n3jigw.naviplayer.feature.loginhistory.impl.databinding.ItemLoginHistoryBinding

/**
 * 登录历史列表适配器
 */
class LoginHistoryAdapter : BaseQuickAdapter<SelectableLoginHistory, VH>() {

    var inDeleteMode = false
    var onItemCheckedChanged: ((SelectableLoginHistory, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val viewHolder = VH(parent)
        viewHolder.addListener(this)
        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: SelectableLoginHistory?) {
        if (item == null) return
        holder.bind(item, inDeleteMode)
    }

    class VH(
        parent: ViewGroup, val binding: ItemLoginHistoryBinding = ItemLoginHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SelectableLoginHistory, inDeleteMode: Boolean) {
            binding.cbDelete.visibility = if (inDeleteMode) View.VISIBLE else View.GONE
            binding.tvServerAddress.text = data.loginHistory.server
            binding.tvUsername.text = data.loginHistory.username
            binding.tvTime.text = data.loginHistory.time
            if (binding.cbDelete.isChecked != data.isChecked) {
                binding.cbDelete.isChecked = data.isChecked
            }
        }

        fun addListener(adapter: LoginHistoryAdapter) {
            // 添加点击事件
            binding.cbDelete.setOnCheckedChangeListener(null)
            binding.cbDelete.setOnCheckedChangeListener { _, isChecked ->
                val currentPosition = bindingAdapterPosition
                if (currentPosition == RecyclerView.NO_POSITION) return@setOnCheckedChangeListener
                adapter.getItem(currentPosition).let {
                    if (it.isChecked != isChecked) {
                        adapter.onItemCheckedChanged?.invoke(it, isChecked)
                    }
                }
            }
        }
    }
}