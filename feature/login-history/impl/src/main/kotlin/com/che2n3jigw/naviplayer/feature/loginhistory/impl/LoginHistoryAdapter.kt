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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.che2n3jigw.naviplayer.feature.loginhistory.impl.LoginHistoryAdapter.VH
import com.che2n3jigw.naviplayer.feature.loginhistory.impl.databinding.ItemLoginHistoryBinding

/**
 * 登录历史列表适配器
 */
class LoginHistoryAdapter : BaseQuickAdapter<SelectableLoginHistory, VH>(diffCallback = object :
    DiffUtil.ItemCallback<SelectableLoginHistory>() {
    override fun areItemsTheSame(
        oldItem: SelectableLoginHistory,
        newItem: SelectableLoginHistory
    ): Boolean {
        val old = oldItem.loginHistory
        val new = newItem.loginHistory
        return old.server == new.server && old.username == new.username
    }

    override fun areContentsTheSame(
        oldItem: SelectableLoginHistory,
        newItem: SelectableLoginHistory
    ): Boolean {
        return oldItem.isChecked == newItem.isChecked && oldItem.isEditMode == newItem.isEditMode
    }

    override fun getChangePayload(
        oldItem: SelectableLoginHistory,
        newItem: SelectableLoginHistory
    ): Any? {
        return when {
            oldItem.isChecked != newItem.isChecked -> PAYLOAD_CHECKED
            oldItem.isEditMode != newItem.isEditMode -> PAYLOAD_MODE
            else -> null
        }
    }
}) {

    companion object {
        private const val PAYLOAD_CHECKED = "selected_state"
        private const val PAYLOAD_MODE = "edit_mode"
    }

    var onItemCheckedChanged: ((SelectableLoginHistory, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val viewHolder = VH(parent)
        viewHolder.addListener(this)
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: SelectableLoginHistory?,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                when (it) {
                    PAYLOAD_CHECKED -> {
                        holder.binding.cbDelete.isChecked = item?.isChecked ?: false
                    }

                    PAYLOAD_MODE -> {
                        val edit = item?.isEditMode ?: false
                        holder.binding.cbDelete.visibility = if (edit) View.VISIBLE else View.GONE
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, item, payloads)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: SelectableLoginHistory?) {
        if (item == null) return
        holder.bind(item)
    }

    class VH(
        parent: ViewGroup, val binding: ItemLoginHistoryBinding = ItemLoginHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SelectableLoginHistory) {
            binding.cbDelete.visibility = if (data.isEditMode) View.VISIBLE else View.GONE
            binding.tvServerAddress.text = data.loginHistory.server
            binding.tvUsername.text = data.loginHistory.username
            binding.tvTime.text = data.loginHistory.time
            binding.cbDelete.isChecked = data.isChecked
        }

        fun addListener(adapter: LoginHistoryAdapter) {
            // 添加点击事件
            binding.cbDelete.setOnCheckedChangeListener(null)
            binding.cbDelete.setOnCheckedChangeListener { _, isChecked ->
                val currentPosition = bindingAdapterPosition
                if (currentPosition == RecyclerView.NO_POSITION) return@setOnCheckedChangeListener
                adapter.getItem(currentPosition).let {
                    adapter.onItemCheckedChanged?.invoke(it, isChecked)
                }
            }
        }
    }
}