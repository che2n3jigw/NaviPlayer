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
// 创建时间： 2026/4/24 13:56
package com.che2n3jigw.naviplayer.core.media

import android.content.Context
import android.net.http.HttpEngine
import android.os.Build
import android.os.ext.SdkExtensions
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpEngineDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.datasource.cronet.CronetUtil
import com.che2n3jigw.naviplayer.core.common.Dispatcher
import com.che2n3jigw.naviplayer.core.common.NaviDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 媒体缓存管理
 */
@OptIn(UnstableApi::class)
@Singleton
class MediaCacheManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:Dispatcher(NaviDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        const val CACHE_SIZE = 2 * 1024 * 1024 * 1024L
    }

    /**
     * 创建一个数据库提供者
     */
    private val databaseProvider by lazy { StandaloneDatabaseProvider(context) }

    /**
     * 创建一个缓存对象
     */
    val cache by lazy {
        val downloadDirectory = File(getCachePath())
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }
        SimpleCache(downloadDirectory, LeastRecentlyUsedCacheEvictor(CACHE_SIZE), databaseProvider)
    }

    /**
     * 获取缓存目录的绝对路径
     */
    fun getCachePath(): String {
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        return File(cacheDir, "media_cache").absolutePath
    }

    /**
     * 获取当前已缓存的数据大小（单位：字节）
     */
    fun getCacheSize(): Long {
        return cache.cacheSpace
    }

    /**
     * 清除所有缓存
     */
    suspend fun clearCache() {
        withContext(ioDispatcher) {
            cache.keys.forEach {
                cache.removeResource(it)
            }
        }
    }

    /**
     * 获取支持缓存的数据源工厂
     */
    fun getCacheDataSourceFactory(): DataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(getHttpDataSourceFactory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private fun getHttpDataSourceFactory(): DataSource.Factory {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
            && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.S) >= 7
        ) {
            val httpEngine = HttpEngine.Builder(context).build()
            return HttpEngineDataSource.Factory(httpEngine, Executors.newSingleThreadExecutor())
        }

        val cronetEngine = CronetUtil.buildCronetEngine(context)
        if (cronetEngine != null) {
            return CronetDataSource.Factory(cronetEngine, Executors.newSingleThreadExecutor())
        }

        // The device doesn't support HttpEngine and we failed to instantiate a CronetEngine.
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        CookieHandler.setDefault(cookieManager)
        return DefaultHttpDataSource.Factory()
    }
}