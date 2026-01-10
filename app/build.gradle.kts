plugins {
    alias(libs.plugins.naviplayer.android.application)
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}