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

import android.content.Intent
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.che2n3jigw.naviplayer.core.ui.BaseActivity
import com.che2n3jigw.naviplayer.databinding.ActivityMainBinding
import com.che2n3jigw.naviplayer.feature.player.api.widget.PlayerNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        const val ACTION_TO_PLAY_PAGE = "com.che2n3jigw.naviplayer.SHOW_PLAYER"
    }

    @Inject
    lateinit var playerNavigator: PlayerNavigator

    override fun inflateBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private var navController: NavController? = null

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun initView() {
        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = host.navController
        navController?.let {
            binding.bottomNavView.setupWithNavController(it)
        }

        handleIntent(intent)
    }

    override fun initListener() {
        navController?.addOnDestinationChangedListener { _, _, arguments ->
            val showBottomNav = arguments?.getBoolean("showBottomNav", true) ?: true
            binding.bottomNavView.isVisible = showBottomNav
        }
    }

    override fun subscribeUI() {
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == ACTION_TO_PLAY_PAGE) {
            navController?.let {
                playerNavigator.navigateToPlayer(it)
            }
        }
    }
}