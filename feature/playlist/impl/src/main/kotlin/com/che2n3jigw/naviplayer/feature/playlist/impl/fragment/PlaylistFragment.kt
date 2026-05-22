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
// 创建时间： 2026/5/21 14:02
package com.che2n3jigw.naviplayer.feature.playlist.impl.fragment

import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.playlist.impl.databinding.FragmentPlaylistBinding

/**
 * 歌单详情页
 */
class PlaylistFragment: BaseFragment<FragmentPlaylistBinding>() {
    override fun inflateBinding(): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun subscribeUI() {
    }
}