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
import com.che2n3jigw.naviplayer.feature.playlist.impl.R
import com.che2n3jigw.naviplayer.feature.playlist.impl.databinding.ItemPlaylistsBinding
import com.che2n3jigw.naviplayer.feature.playlist.impl.model.PlaylistItemUiModel

/**
 * 歌单列表适配器
 */
class PlaylistsAdapter :
    ListAdapter<PlaylistItemUiModel, PlaylistsViewHolder>(object :
        DiffUtil.ItemCallback<PlaylistItemUiModel>() {
        override fun areItemsTheSame(
            oldItem: PlaylistItemUiModel,
            newItem: PlaylistItemUiModel
        ): Boolean {
            return oldItem.playlist.id == newItem.playlist.id
        }

        override fun areContentsTheSame(
            oldItem: PlaylistItemUiModel,
            newItem: PlaylistItemUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    var onAddClickListener: ((Playlist) -> Unit)? = null
    var onRemoveClickListener: ((Playlist) -> Unit)? = null
    var onDeleteClickListener: ((Playlist) -> Unit)? = null
    var onItemClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val binding =
            ItemPlaylistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(
            data,
            onAddClickListener,
            onRemoveClickListener,
            onDeleteClickListener,
            onItemClickListener
        )
    }
}

class PlaylistsViewHolder(private val binding: ItemPlaylistsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        data: PlaylistItemUiModel,
        onAddClickListener: ((Playlist) -> Unit)?,
        onRemoveClickListener: ((Playlist) -> Unit)?,
        deleteClickListener: ((Playlist) -> Unit)?,
        onItemClickListener: ((Playlist) -> Unit)?
    ) {
        val playlist = data.playlist
        binding.tvPlaylistName.text = playlist.name
        binding.tvPlaylistSongCount.text = playlist.songCount.toString()
        binding.btnAction.setText(
            when {
                data.showAdd -> R.string.playlist_add
                data.showRemove -> R.string.playlist_remove
                else -> R.string.playlist_delete
            }
        )


        binding.btnAction.setOnClickListener {
            when {
                data.showAdd -> onAddClickListener?.invoke(playlist)
                data.showRemove -> onRemoveClickListener?.invoke(playlist)
                data.showDelete -> deleteClickListener?.invoke(playlist)
            }
        }
        binding.cvPlaylist.setOnClickListener {
            onItemClickListener?.invoke(playlist)
        }
    }
}