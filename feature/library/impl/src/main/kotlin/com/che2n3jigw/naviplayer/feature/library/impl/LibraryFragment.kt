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
package com.che2n3jigw.naviplayer.feature.library.impl

import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.library.impl.databinding.FragmentLibraryBinding
import com.google.android.material.carousel.CarouselLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 音乐库页面
 */
@AndroidEntryPoint
class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {

    private val albumAdapter = LibraryItemAdapter()
    private val songAdapter = SongItemAdapter()

    private val viewmodel: LibraryViewModel by viewModels()

    override fun inflateBinding() = FragmentLibraryBinding.inflate(layoutInflater)

    override fun initView() {
        binding.rvAlbum.apply {
            layoutManager = CarouselLayoutManager()
            isNestedScrollingEnabled = false
            adapter = albumAdapter
        }

        binding.rvSong.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            adapter = songAdapter
        }
    }

    override fun initListener() {
        albumAdapter.itemClickListener = { _, position ->
            binding.rvAlbum.smoothScrollToPosition(position)
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.uiState.collect {
                    binding.loading.isVisible = it is LibraryUiState.Loading
                    binding.groupSuccess.isVisible = it is LibraryUiState.Success
                    when (it) {
                        is LibraryUiState.Success -> {
                            albumAdapter.submitList(it.albums)
                            songAdapter.submitList(it.randomSongs)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.ibSearch.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }
}