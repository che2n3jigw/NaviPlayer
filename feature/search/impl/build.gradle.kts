plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.search.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)

    implementation(projects.feature.search.api)

    implementation(libs.androidx.navigation.fragment.ktx)
}