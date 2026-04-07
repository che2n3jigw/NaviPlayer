plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.datastore"
}

dependencies {
    api(libs.androidx.datastore)
    api(projects.core.model)

    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.common)
}