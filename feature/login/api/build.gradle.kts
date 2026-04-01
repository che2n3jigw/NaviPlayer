plugins {
    alias(libs.plugins.naviplayer.android.library)
}

android {
    namespace = "com.che2n3jigw.naviplayer.feature.login.api"
}

dependencies {
    implementation(projects.core.navigation)
}