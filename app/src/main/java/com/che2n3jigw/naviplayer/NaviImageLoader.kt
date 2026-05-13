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

package com.che2n3jigw.naviplayer

import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 全局Coil的图片加载
 */
class NaviImageLoader(private val context: Context) : ImageLoaderFactory {
    companion object {
        private val DEFAULT_FINGERPRINTS = setOf(
            "e6d83a033593d24fc5418564eb62e65e"
        )
    }

    override fun newImageLoader(): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                val body = response.body

                // 增加基础过滤：仅处理成功的封面图请求
                if (response.isSuccessful && body != null
                    && request.url.toString().contains("getCoverArt")
                ) {
                    val source = body.source()
                    source.request(128)
                    val byteCount = minOf(source.buffer.size, 128L)
                    val fingerprint = source.buffer.snapshot(byteCount.toInt()).md5().hex()

                    if (DEFAULT_FINGERPRINTS.contains(fingerprint)) {
                        // 必须先关闭旧的 body 释放连接
                        body.close()

                        return@addInterceptor response.newBuilder()
                            .code(404)
                            .message("Ignored default cover")
                            .body("".toResponseBody(null))
                            .build()
                    }
                }
                response
            }
            .build()

        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient)
            .crossfade(true)
            .build()
    }
}