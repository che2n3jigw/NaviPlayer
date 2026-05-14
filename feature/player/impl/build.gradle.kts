plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.player.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)

    implementation(libs.coil.kt)
}