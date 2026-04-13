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
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.che2n3jigw.naviplayer.core.ui.BaseFragment
import com.che2n3jigw.naviplayer.core.ui.util.ClickEffectUtil
import com.che2n3jigw.naviplayer.feature.me.impl.databinding.FragmentMeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 我的页面
 */
@AndroidEntryPoint
class MeFragment : BaseFragment<FragmentMeBinding>() {

    private val snackBar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE).apply {
            setAnchorView(binding.nsvContent)
        }
    }

    private val viewModel: MeViewModel by viewModels()

    override fun inflateBinding() = FragmentMeBinding.inflate(layoutInflater)

    override fun initView() {
        ClickEffectUtil.applyScale(binding.ivAvatar)
        ClickEffectUtil.applyScale(binding.ivFavoritePlay)
        ClickEffectUtil.applyScale(binding.ivRecentlyPlay)
        ClickEffectUtil.applyScale(binding.viewStatAlbums)
        ClickEffectUtil.applyScale(binding.viewStatArtists)
        ClickEffectUtil.applyScale(binding.viewStatLists)
        ClickEffectUtil.applyScale(binding.viewStatOffline)
        ClickEffectUtil.applyScale(binding.ivNext)
        ClickEffectUtil.applyScale(binding.ivPlay)
        ClickEffectUtil.applyScale(binding.ivPrevious)
        ClickEffectUtil.applyEnlarge(binding.ivFavoriteCover)
    }

    override fun initListener() {
        binding.ivFavoritePlay.setOnClickListener {
            snackBar.show()
        }
    }

    override fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 1. 只有登录状态变化时才更新 SnackBar
                launch {
                    viewModel.uiState
                        .map { it.isLoggedIn }
                        .distinctUntilChanged()
                        .collect { isLoggedIn -> updateLoginStatus(isLoggedIn) }
                }

                // 2. 只有统计数据变化时才更新统计区域
                launch {
                    viewModel.uiState
                        .map { listOf(it.albumCount, it.artistCount, it.listCount, it.offlineSize) }
                        .distinctUntilChanged()
                        .collect { updateStatsSection(viewModel.uiState.value) }
                }

                // 3. 只有用户信息/收藏变化时刷新
                launch {
                    viewModel.uiState
                        .map {
                            Triple(
                                it.avatar, it.favouriteCover, it.favouriteCount
                            ) to it.lastPlayTime
                        }
                        .distinctUntilChanged()
                        .collect { updateUserSection(viewModel.uiState.value) }
                }

                // 4. 只有播放状态变化时刷新迷你播放器
                launch {
                    viewModel.uiState
                        .map {
                            Triple(
                                it.currentPlayCover, it.currentPlayName, it.currentPlaySinger
                            )
                        }
                        .distinctUntilChanged()
                        .collect { updateMiniPlayerSection(viewModel.uiState.value) }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: Insets) {
        binding.ivAvatar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
    }

    override fun onPause() {
        super.onPause()
        snackBar.dismiss()
    }

    private fun updateUserSection(state: MeUiState) {
        binding.ivAvatar.load(state.avatar) {
            placeholder(R.drawable.me_placeholder_avatar)
            error(R.drawable.me_placeholder_avatar)
        }
        binding.ivFavoriteCover.load(state.favouriteCover) {
            placeholder(R.drawable.me_placeholder_favourite_song)
            error(R.drawable.me_placeholder_favourite_song)
        }

        binding.tvTrackCount.text =
            getString(R.string.me_library_tracks_count, state.favouriteCount)

        // 最近播放时间
        binding.tvActive.apply {
            isVisible = state.lastPlayTime.isNotEmpty()
            if (state.lastPlayTime.isNotEmpty()) {
                text = getString(R.string.me_library_active, state.lastPlayTime)
            }
        }
    }

    private fun updateStatsSection(state: MeUiState) {
        binding.viewStatAlbums.setData(
            R.drawable.me_ic_albums,
            state.albumCount.toString(),
            getString(R.string.me_albums)
        )
        binding.viewStatArtists.setData(
            R.drawable.me_ic_artists,
            state.artistCount.toString(),
            getString(R.string.me_artists)
        )
        binding.viewStatLists.setData(
            R.drawable.me_ic_lists,
            state.listCount.toString(),
            getString(R.string.me_lists)
        )
        binding.viewStatOffline.setData(
            R.drawable.me_ic_offline,
            state.offlineSize,
            getString(R.string.me_offline)
        )
    }

    private fun updateMiniPlayerSection(state: MeUiState) {
        binding.ivCoverPlaying.load(state.currentPlayCover) {
            placeholder(R.drawable.me_placeholder_mini_player_cover)
            error(R.drawable.me_placeholder_mini_player_cover)
        }
        binding.tvSingName.text = state.currentPlayName
        binding.tvSinger.text = state.currentPlaySinger
    }

    private fun updateLoginStatus(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            snackBar.dismiss()
        } else {
            snackBar.setText(R.string.me_not_login)
            snackBar.setAction(R.string.me_to_login) {}
            snackBar.show()
        }
    }
}