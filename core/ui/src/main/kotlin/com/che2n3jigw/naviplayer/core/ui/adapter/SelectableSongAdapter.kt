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
package com.che2n3jigw.naviplayer.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.che2n3jigw.naviplayer.core.model.SelectableItem
import com.che2n3jigw.naviplayer.core.model.Song
import com.che2n3jigw.naviplayer.core.ui.R
import com.che2n3jigw.naviplayer.core.ui.databinding.ItemSongBinding

/**
 * 可选择的歌曲列表适配器
 */
class SelectableSongAdapter : ListAdapter<SelectableItem<Song>, SelectableSongViewHolder>(
    object : DiffUtil.ItemCallback<SelectableItem<Song>>() {
        override fun areItemsTheSame(
            oldItem: SelectableItem<Song>,
            newItem: SelectableItem<Song>
        ): Boolean {
            return oldItem.data.id == newItem.data.id
        }

        override fun areContentsTheSame(
            oldItem: SelectableItem<Song>,
            newItem: SelectableItem<Song>
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    var itemClickListener: ((Song, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableSongViewHolder {
        val inflate = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectableSongViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: SelectableSongViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener)
    }
}

class SelectableSongViewHolder(private val binding: ItemSongBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SelectableItem<Song>, itemClickListener: ((Song, Int) -> Unit)?) {
        val song = item.data
        binding.tvSongName.text = song.name
        binding.tvArtist.text = song.singer
        binding.ivCover.load(song.imageUrl) {
            error(R.drawable.default_error_cover)
        }
        binding.cvSong.apply {
            isChecked = item.isSelected
            setOnClickListener { itemClickListener?.invoke(song, bindingAdapterPosition) }
        }
    }
}