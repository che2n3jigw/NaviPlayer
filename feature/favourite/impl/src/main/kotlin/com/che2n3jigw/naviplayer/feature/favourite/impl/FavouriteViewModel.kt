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
// 创建时间： 2026/5/25 17:15
package com.che2n3jigw.naviplayer.feature.favourite.impl

import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.core.model.Song
import com.che2n3jigw.naviplayer.feature.songlist.api.SongListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    naviMediaManager: NaviMediaManager,
    private val subsonicRepository: SubsonicRepository
) : SongListViewModel(naviMediaManager) {

    private val _songList = MutableStateFlow<List<Song>?>(null)

    override val songList: StateFlow<List<Song>?> = _songList.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _songList.update { subsonicRepository.getFavouriteList() }
        }
    }
}