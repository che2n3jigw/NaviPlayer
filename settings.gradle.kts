pluginManagement {
    repositories {
        includeBuild("build-logic")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "naviplayer"
include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// core
include(":core:model")
include(":core:database")
include(":core:network")
include(":core:media")
include(":core:data")
include(":core:common")
include(":core:navigation")
include(":core:ui")

// feature
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:login-history:impl")
include(":feature:player:impl")
include(":feature:playlist:impl")
include(":feature:search:impl")
include(":feature:setting:impl")
