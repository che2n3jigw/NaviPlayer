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
// 创建时间： 2026/4/1 16:19
package com.che2n3jigw.naviplayer.feature.me.impl

import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.me.impl.databinding.FragmentMeBinding

/**
 * 我的页面
 */
class MeFragment : BaseFragment<FragmentMeBinding>() {

    override fun inflateBinding() = FragmentMeBinding.inflate(layoutInflater)

    override fun initView() {
        binding.ivAvatar.setImageResource(R.drawable.me_placeholder_avatar)
        binding.viewStatAlbums.setData(R.drawable.me_ic_albums, "10000", "Albums")
        binding.viewStatArtists.setData(R.drawable.me_ic_artists, "14", "Artists")
        binding.viewStatLists.setData(R.drawable.me_ic_lists, "10", "Lists")
        binding.viewStatOffline.setData(R.drawable.me_ic_offline, "2.4G", "Offline")
        binding.tvTrackCount.text = getString(R.string.me_library_tracks_count, 1081)
        binding.ivFavoriteCover.setImageResource(R.drawable.me_placeholder_favourite_song)
        binding.tvActive.text = getString(R.string.me_library_active, "2 分钟")

        binding.ivCoverPlaying.setImageResource(R.drawable.me_placeholder_avatar)
        binding.tvSingName.text = "sing name"
        binding.tvSinger.text = "singer"
    }

    override fun initListener() {}

    override fun subscribeUI() {}

    override fun onApplyWindowInsets(insets: Insets) {
        binding.ivAvatar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }
}