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
package com.che2n3jigw.naviplayer.feature.library.impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.che2n3jigw.naviplayer.core.model.Song

class SongItemAdapter : ListAdapter<Song, SongItemViewHolder>(
    object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class SongItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Song) {
        itemView.findViewById<TextView>(R.id.tv_song_name).text = item.name
        itemView.findViewById<TextView>(R.id.tv_artist).text = item.singer
        itemView.findViewById<ImageView>(R.id.iv_cover).load(item.imageUrl) {
            error(R.drawable.library_default_song_cover)
        }
        itemView.setOnClickListener {

        }
    }
}