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
// 创建时间： 2026/5/21 16:09
package com.che2n3jigw.naviplayer.feature.playlist.impl.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.che2n3jigw.naviplayer.core.model.Playlist
import com.che2n3jigw.naviplayer.feature.playlist.impl.databinding.ItemPlaylistsBinding

/**
 * 歌单列表适配器
 */
class PlaylistsAdapter :
    ListAdapter<Playlist, PlaylistsViewHolder>(object : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }) {

    var onDeleteClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val binding =
            ItemPlaylistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, onDeleteClickListener)
    }
}

class PlaylistsViewHolder(private val binding: ItemPlaylistsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Playlist, deleteClickListener: ((Playlist) -> Unit)?) {
        binding.tvPlaylistName.text = data.name
        binding.tvPlaylistSongCount.text = data.songCount.toString()
        binding.btnDelete.setOnClickListener {
            deleteClickListener?.invoke(data)
        }
    }
}