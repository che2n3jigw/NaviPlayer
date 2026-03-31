plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.login.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.navigation)

    implementation(projects.feature.login.api)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
}