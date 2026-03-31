plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.common)
}