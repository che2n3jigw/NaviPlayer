plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.media"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.datasource.cronet)
    implementation(libs.kotlinx.coroutines.guava)
    api(libs.androidx.media3.session)
}