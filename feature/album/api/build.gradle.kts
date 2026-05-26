plugins {
    alias(libs.plugins.naviplayer.android.feature.api)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.album.api"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)

    implementation(libs.coil.kt)
}