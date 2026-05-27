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
// 创建时间： 2026/5/26 22:20
package com.che2n3jigw.naviplayer.feature.album.impl.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.feature.album.api.AlbumNavigator
import com.che2n3jigw.naviplayer.feature.album.impl.R
import com.che2n3jigw.naviplayer.feature.album.impl.adapter.AlbumListAdapter
import com.che2n3jigw.naviplayer.feature.album.impl.databinding.FragmentAlbumListBinding
import com.che2n3jigw.naviplayer.feature.album.impl.viewmodel.AlbumListUiState
import com.che2n3jigw.naviplayer.feature.album.impl.viewmodel.AlbumListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 专辑列表页面
 */
@AndroidEntryPoint
class AlbumListFragment : BaseFragment<FragmentAlbumListBinding>() {

    private val viewModel by viewModels<AlbumListViewModel>()

    @Inject
    lateinit var albumNavigator: AlbumNavigator

    private val albumListAdapter = AlbumListAdapter()

    override fun inflateBinding(): FragmentAlbumListBinding {
        return FragmentAlbumListBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()
    }

    override fun initView() {
        binding.rvAlbum.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = albumListAdapter
        }
        binding.toolbar.setTitle(R.string.album_list_title)
    }

    override fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        albumListAdapter.onItemClickListener = { id ->
            albumNavigator.navigateToAlbumDetail(findNavController(), id)
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.loading.isVisible = it is AlbumListUiState.Loading
                    binding.rvAlbum.isVisible = it is AlbumListUiState.Success
                    if (it is AlbumListUiState.Success) {
                        albumListAdapter.submitList(it.albumContentList)
                    }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        super.onApplyWindowInsets(insets)
        binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }
}