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
// 创建时间： 2026/5/27 11:53
package com.che2n3jigw.naviplayer.feature.album.impl.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.che2n3jigw.naviplayer.core.ui.R
import com.che2n3jigw.naviplayer.feature.album.api.AlbumItem
import com.che2n3jigw.naviplayer.feature.album.impl.databinding.ItemAlbumListBinding

class AlbumListAdapter :
    ListAdapter<AlbumItem.Content, AlbumListAdapter.AlbumItemViewHolder>(object :
        DiffUtil.ItemCallback<AlbumItem.Content>() {
        override fun areItemsTheSame(
            oldItem: AlbumItem.Content,
            newItem: AlbumItem.Content
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AlbumItem.Content,
            newItem: AlbumItem.Content
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumItemViewHolder {
        val binding =
            ItemAlbumListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumItemViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: AlbumItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AlbumItemViewHolder(
        private val binding: ItemAlbumListBinding,
        private val onItemClickListener: ((String) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlbumItem.Content) {
            binding.ivCover.load(item.coverUrl) {
                error(R.drawable.default_error_cover)
            }
            binding.tvTitle.text = item.title
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item.id)
            }
        }
    }
}