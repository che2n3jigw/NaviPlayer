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
// 创建时间： 2026/5/26 11:35
package com.che2n3jigw.naviplayer.feature.album.api

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.che2n3jigw.naviplayer.feature.album.api.databinding.ItemAlbumCarouselBinding

/**
 * 专辑轮播适配器(横向滚动的视觉效果)
 */
class AlbumCarouselAdapter : ListAdapter<AlbumItem, AlbumCarouselViewHolder>(
    object : DiffUtil.ItemCallback<AlbumItem>() {
        override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            if (oldItem is AlbumItem.Content && newItem is AlbumItem.Content) {
                return oldItem.id == newItem.id
            }
            return false
        }

        override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    var itemClickListener: ((AlbumItem, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCarouselViewHolder {

        val binding =
            ItemAlbumCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AlbumCarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumCarouselViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener)
    }
}

class AlbumCarouselViewHolder(private val binding: ItemAlbumCarouselBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AlbumItem, itemClickListener: ((AlbumItem, Int) -> Unit)?) {
        if (item is AlbumItem.Content) {
            binding.ivCover.load(item.coverUrl) { error(R.drawable.album_default_cover) }
            binding.tvTitle.text = item.title
        } else if (item is AlbumItem.More) {
            binding.ivCover.load(R.drawable.album_ic_more)
            binding.tvTitle.setText(R.string.album_more)
        }

        binding.root.setOnHoverListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> v.setAlpha(0.5f)
                MotionEvent.ACTION_HOVER_EXIT -> v.setAlpha(1.0f)
            }
            return@setOnHoverListener false
        }
        binding.root.setOnClickListener {
            itemClickListener?.invoke(item, bindingAdapterPosition)
        }
    }
}