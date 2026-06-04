plugins {
    alias(libs.plugins.naviplayer.android.application)
    alias(libs.plugins.naviplayer.hilt)
}

android {
    namespace = "com.che2n3jigw.naviplayer"

    defaultConfig {
        applicationId = "com.che2n3jigw.naviplayer"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.feature.library.impl)
    implementation(projects.feature.me.impl)
    implementation(projects.feature.setting.impl)

    implementation(projects.feature.login.impl)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.playlist.impl)
    implementation(projects.feature.favourite.impl)
    implementation(projects.feature.recent.impl)
    implementation(projects.feature.player.impl)

    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.core.media)
    implementation(projects.core.common)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.coil.kt)
}