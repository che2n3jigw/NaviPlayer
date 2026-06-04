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
// 创建时间： 2026/3/30 14:17
package com.che2n3jigw.naviplayer

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.che2n3jigw.naviplayer.core.ui.BaseActivity
import com.che2n3jigw.naviplayer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun inflateBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private var navController: NavController? = null
    override fun initView() {
        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = host.navController
        navController?.let {
            binding.bottomNavView.setupWithNavController(it)
        }
    }

    override fun initListener() {
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                com.che2n3jigw.naviplayer.feature.player.impl.R.id.player_dest,
                com.che2n3jigw.naviplayer.feature.login.impl.R.id.login_dest -> {
                    binding.bottomNavView.visibility = View.GONE
                }

                else -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }

    override fun subscribeUI() {
    }
}