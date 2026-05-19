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
// 创建时间： 2026/4/27 11:30
package com.che2n3jigw.naviplayer.feature.library.impl.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.che2n3jigw.naviplayer.feature.library.impl.bean.LibraryItem
import com.che2n3jigw.naviplayer.feature.library.impl.R

class LibraryItemAdapter : ListAdapter<LibraryItem, LibraryItemViewHolder>(
    object : DiffUtil.ItemCallback<LibraryItem>() {
        override fun areItemsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    var itemClickListener: ((LibraryItem, Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_library, parent, false)
        return LibraryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener)
    }
}

class LibraryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: LibraryItem, itemClickListener: ((LibraryItem, Int) -> Unit)?) {
        itemView.findViewById<ImageView>(R.id.iv_cover).load(item.coverUrl) {
            error(R.drawable.library_default_cover)
        }
        itemView.findViewById<TextView>(R.id.tv_title).text = item.title
        itemView.setOnHoverListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> v.setAlpha(0.5f)
                MotionEvent.ACTION_HOVER_EXIT -> v.setAlpha(1.0f)
            }
            return@setOnHoverListener false
        }
        itemView.setOnClickListener {
            itemClickListener?.invoke(item, bindingAdapterPosition)
        }
    }
}