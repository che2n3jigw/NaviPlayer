plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.album.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.media)
    implementation(projects.core.data)

    implementation(projects.feature.album.api)
    implementation(projects.feature.songlist.api)

    implementation(libs.androidx.navigation.fragment.ktx)
}