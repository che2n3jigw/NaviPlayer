plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.loginhistory.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.baserecyclerviewadapterhelper4)
    implementation(libs.androidx.activity.ktx)

    // lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.common)

    implementation(projects.feature.login.api)
    implementation(libs.androidx.navigation.fragment.ktx)
}