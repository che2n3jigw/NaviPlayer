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
    implementation(projects.feature.loginHistory.impl)
    implementation(projects.feature.login.impl)

    implementation(projects.core.ui)
    implementation(projects.core.navigation)

    implementation(libs.androidx.navigation.fragment.ktx)
}