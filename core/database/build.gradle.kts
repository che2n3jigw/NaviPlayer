plugins {
    alias(libs.plugins.naviplayer.android.library)
    alias(libs.plugins.naviplayer.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "com.che2n3jigw.naviplayer.core.database"

    // 使用room插件配置room编译器选项
    room {
        // 启用将数据库架构导出到给定目录的JSON文件的功能
        // 参考[room数据库迁移](https://developer.android.com/training/data-storage/room/migrating-db-versions?hl=zh-cn#test)
        schemaDirectory("$projectDir/schemas")
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(projects.core.model)

    androidTestImplementation(libs.test.androidx.core)
    androidTestImplementation(libs.test.androidx.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}