plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.data"
}

dependencies {
    api(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.common)
    implementation(libs.android.open.subsonic.api)
}