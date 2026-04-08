/*
 * Copyright (c) 2026 che2n3jigw.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

// 作者: che2n3jigw
// 邮箱: che2n3jigw@163.com
// 博客: che2n3jigw.github.io
// 创建时间： 1/10/26
package com.che2n3jigw.naviplayer.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * 为应用了 Android 插件的模块配置基础的 Kotlin 选项。
 * @param commonExtension 来自 Android Gradle 插件的通用配置扩展。
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        // 设置编译所用的 Android SDK 版本
        compileSdk = 36

        defaultConfig {
            // 设置应用支持的最低 Android SDK 版本
            minSdk = 24
        }

        compileOptions {
            // 通过 "desugaring"（脱糖），我们可以在较低版本的安卓系统上使用 Java 11 的 API
            // 参考链接：https://developer.android.com/studio/write/java11-minimal-support-table
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            // 启用核心库脱糖功能
            isCoreLibraryDesugaringEnabled = true
        }
    }

    // 调用通用的 Kotlin 配置函数
    configureKotlin<KotlinAndroidProjectExtension>()

    dependencies {
        // 添加核心库脱糖所需的依赖
        "coreLibraryDesugaring"(libs.findLibrary("android.desugarJdkLibs").get())
    }
}

/**
 * 为纯 JVM（非 Android）模块配置基础的 Kotlin 选项。
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        // 设置 Java 源码和目标字节码的版本为 Java 11
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // 调用通用的 Kotlin 配置函数
    configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * 配置所有 Kotlin 模块通用的基础选项。
 * 这是一个内联的泛型函数，以适用于不同类型的 Kotlin 项目（Android, JVM 等）。
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // 将所有 Kotlin 警告视为错误（默认禁用）
    // 你可以在 `~/.gradle/gradle.properties` 文件中添加 `warningsAsErrors=true` 来覆盖此设置
    val warningsAsErrors = providers.gradleProperty("warningsAsErrors").map {
        it.toBoolean()
    }.orElse(false)

    // 根据项目类型获取对应的编译器选项
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("不支持的项目扩展类型 $this ${T::class}")
    }.apply {
        // TODO: 升级到 AGP 9.0 后，移除 languageVersion 和 coreLibrariesVersion
        // 设置 Kotlin 语言版本
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        // 强制指定 Kotlin 核心库的版本
        coreLibrariesVersion = "2.2.21"
        // 设置生成的字节码与 JVM 11 兼容
        jvmTarget = JvmTarget.JVM_11
        // 应用上面获取的 `warningsAsErrors` 设置
        allWarningsAsErrors = warningsAsErrors
        freeCompilerArgs.add(
            // 允许在项目中使用实验性的协程 API (例如 Flow)，而无需在代码中添加 @OptIn 注解
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
        freeCompilerArgs.add(
            /**
             * 在 Phase 3 之后可以移除这个参数。
             * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-consistent-copy-visibility/#deprecation-timeline
             *
             * 弃用时间线：
             * Phase 3 (预计在 Kotlin 2.2 或 2.3 版本)。
             * 默认行为将发生改变。
             * 除非使用 `ExposedCopyVisibility`，否则生成的 `copy` 方法将具有与主构造函数相同的可见性。
             * 二进制签名会改变。声明处的错误将不再报告。
             * `-Xconsistent-data-class-copy-visibility` 编译器标志和 `ConsistentCopyVisibility` 注解将不再是必需的。
             */
            "-Xconsistent-data-class-copy-visibility"
        )
        // 启用实验性的 Kotlin 时间 API(kotlin.time.Instant)
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}
