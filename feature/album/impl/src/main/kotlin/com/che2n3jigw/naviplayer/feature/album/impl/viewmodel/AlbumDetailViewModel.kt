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

package com.che2n3jigw.naviplayer.feature.album.impl.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.che2n3jigw.naviplayer.core.data.repository.SubsonicRepository
import com.che2n3jigw.naviplayer.core.media.NaviMediaManager
import com.che2n3jigw.naviplayer.feature.songlist.api.SongListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    naviMediaManager: NaviMediaManager,
    private val subsonicRepository: SubsonicRepository
) : SongListViewModel(naviMediaManager) {

    // 专辑id
    private val _albumId = MutableStateFlow<String?>(null)

    override val songList = combine(
        _albumId,
        refreshTrigger.onStart { emit(Unit) }
    ) { id, _ ->
        id
    }.transformLatest { id ->
        // 第一步：先发射 null，父类会监听到并显示 Loading
        emit(null)

        // 第二步：根据 ID 获取数据
        val result = when {
            id == null -> null
            id.isEmpty() -> emptyList()
            else -> subsonicRepository.getAlbumDetail(id)
        }

        // 第三步：发射最终结果
        emit(result)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun parseBundle(bundle: Bundle?) {
        _albumId.update { bundle?.getString("id") ?: "" }
    }
}