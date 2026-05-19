plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.search.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)
}