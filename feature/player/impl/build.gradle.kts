plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.player.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)

    implementation(projects.feature.player.api)

    implementation(libs.coil.kt)

    implementation(libs.androidx.navigation.fragment.ktx)
}