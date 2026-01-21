plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.common"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}