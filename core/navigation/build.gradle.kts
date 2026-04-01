plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.navigation"
}

dependencies {
    api(libs.androidx.navigation.common)
    api(libs.androidx.navigation.runtime.ktx)
}