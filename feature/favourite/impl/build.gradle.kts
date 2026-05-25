plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.favourite.impl"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.media)
    implementation(projects.core.data)

    implementation(projects.feature.favourite.api)
    implementation(projects.feature.songlist.api)

    implementation(libs.androidx.navigation.fragment.ktx)
}