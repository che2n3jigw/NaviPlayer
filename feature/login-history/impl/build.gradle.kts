plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.loginhistory.impl"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.baserecyclerviewadapterhelper4)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}