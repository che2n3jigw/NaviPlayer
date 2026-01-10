import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}
group = "com.che2n3jigw.naviplayer.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

// 配置 Gradle 任务
tasks {
    // 配置 `validatePlugins` 任务，该任务由 `kotlin-dsl` 插件提供，用于校验自定义 Gradle 插件的声明
    validatePlugins {
        // 启用更严格的验证规则，进行更全面的检查，确保插件元数据正确无误
        enableStricterValidation = true
        // 如果在验证过程中发现任何警告，就让构建失败。这有助于强制执行更高的代码质量标准
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.naviplayer.android.application.map { it.pluginId }.get()
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.naviplayer.android.library.map { it.pluginId }.get()
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.naviplayer.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
