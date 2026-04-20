plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.media"
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.androidx.media3.exoplayer)
    api(libs.androidx.media3.session)
}