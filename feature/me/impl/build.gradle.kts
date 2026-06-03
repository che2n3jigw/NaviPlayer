plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.me.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.core.media)

    implementation(projects.feature.player.api)
    implementation(projects.feature.playlist.api)
    implementation(projects.feature.favourite.api)
    implementation(projects.feature.recent.api)
    implementation(projects.feature.login.api)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt)
}