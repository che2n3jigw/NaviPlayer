plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.recent.impl"
}

dependencies {
    implementation(projects.feature.recent.api)
    implementation(projects.feature.songlist.api)

    implementation(projects.core.ui)
    implementation(projects.core.media)
    implementation(projects.core.data)

    implementation(libs.androidx.navigation.fragment.ktx)
}