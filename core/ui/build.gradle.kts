plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.ui"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.activity.ktx)
    api(libs.coil.kt)

    implementation(projects.core.model)
}