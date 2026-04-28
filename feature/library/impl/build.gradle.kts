plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.library.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.core.media)
    implementation(projects.core.data)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.coil.kt)
}