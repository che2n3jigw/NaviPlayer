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
// 创建时间： 2026/5/19 16:20
package com.che2n3jigw.naviplayer.core.common.utils

import android.content.Context
import com.che2n3jigw.naviplayer.core.common.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeUtils @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    /**
     * 将毫秒时间戳转换为“多久之前”的描述
     */
    fun toTimeAgo(time: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - time

        // 处理未来时间或极短时间
        if (diff < 60_000) return context.getString(R.string.time_just_now)

        val minutes = (diff / 60_000).toInt()
        if (minutes < 30) {
            return context.getString(R.string.time_minutes_ago, minutes)
        }
        if (minutes < 60) return context.getString(R.string.time_half_hour_ago)

        val hours = minutes / 60
        if (hours < 24) {
            return context.getString(R.string.time_hours_ago, hours)
        }

        val days = hours / 24
        if (days < 7) {
            return context.getString(R.string.time_days_ago, days)
        }
        if (days < 30) {
            val weeks = days / 7
            return context.getString(R.string.time_weeks_ago, weeks)
        }

        return context.getString(R.string.time_long_ago)
    }

    /**
     * 秒转时间文本
     */
    fun toTimeText(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }
}
