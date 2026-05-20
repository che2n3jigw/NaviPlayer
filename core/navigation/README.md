# 模块化导航开发指南 (API-IMPL 模式)

为了实现模块间的彻底解耦，本项目采用 **接口下沉 (api)** 与 **实现分离 (impl)** 的导航方案。跨模块跳转通过 **Navigation DeepLink** 实现，从而避免模块间的直接代码引用。

---

## 1. 核心导航逻辑 (以 Search 模块为例)

### 第一步：创建 API 模块 (`feature:search:api`)
该模块仅定义能力，不包含具体实现。

1. **应用插件**：
   在 `build.gradle.kts` 中应用：
   ```kotlin
   plugins {
       alias(libs.plugins.naviplayer.android.feature.api)
   }
   ```

2. **定义跳转接口**：
   ```kotlin
   interface SearchNavigator {
       fun navigateToSearch(navController: NavController)
   }
   ```

3. **声明 DeepLink 常量**：
   统一使用项目定义的 Scheme。
   ```kotlin
   object SearchDeepLink {
       const val SEARCH = "naviplayer://feature/search"
   }
   ```

---

### 第二步：创建实现模块 (`feature:search:impl`)
该模块包含具体的业务 Fragment 和路由逻辑。

1. **build.gradle.kts 配置**：
   ```kotlin
   dependencies {
       implementation(projects.feature.search.api) 
       implementation(libs.androidx.navigation.fragment.ktx)
   }
   ```

2. **实现路由接口 (`SearchNavigatorImpl`)**：
   ```kotlin
   class SearchNavigatorImpl @Inject constructor() : SearchNavigator {
       override fun navigateToSearch(navController: NavController) {
           val request = NavDeepLinkRequest.Builder
               .fromUri(SearchDeepLink.SEARCH.toUri())
               .build()
           navController.navigate(request, CommonNavOptions.slideInOptions)
       }
   }
   ```

3. **Hilt 依赖绑定**：
   ```kotlin
   @Module
   @InstallIn(SingletonComponent::class)
   abstract class SearchNavigationModule {
       @Binds
       abstract fun bindSearchNavigator(impl: SearchNavigatorImpl): SearchNavigator
   }
   ```

4. **配置导航图 (XML)**：
   在 Fragment 节点下增加 `<deepLink>`，确保 `app:uri` 与 API 模块定义的常量一致。
   ```xml
    <navigation xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/login_graph"
        app:startDestination="@id/login_dest">

        <fragment
            android:id="@+id/login_dest"
            android:name="com.che2n3jigw.naviplayer.feature.search.impl.SearchFragment"
            android:label="SearchFragment"
            tools:layout="@layout/fragment_search">

            <deepLink app:uri="naviplayer://feature/search" />

        </fragment>
    </navigation>
   ```
---

## 2. 如何在其他模块调用 (跨模块跳转)

当 `Library` 模块需要跳转到 `Search` 模块时：

1. **添加 API 依赖**：
   ```kotlin
   // feature/library/impl/build.gradle.kts
   dependencies {
       implementation(projects.feature.search.api) // 仅依赖 API，不依赖实现
   }
   ```

2. **Fragment 中使用**：
   ```kotlin
   @AndroidEntryPoint
   class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {
       
       @Inject
       lateinit var searchNavigator: SearchNavigator

       fun onSearchButtonClick() {
           // 接口式跳转，Library 模块完全感知不到 SearchFragment 的存在
           searchNavigator.navigateToSearch(findNavController())
       }
   }
   ```
   
---

## 3. 注意事项 (Best Practices)

1. **Scheme 规范**：全项目统一使用 `naviplayer://` 作为 DeepLink 开头，避免使用 `android-app://` 等过长字符串。
2. **解耦原则**：`impl` 模块禁止作为其他模块的依赖。所有跨模块通信必须通过 `api` 模块定义的接口。
3. **转场动画**：请优先使用 `core:navigation` 中提供的 `CommonNavOptions`，以保证全应用 UI 体验的一致性。
4. **Hilt 作用域**：`Navigator` 接口的实现类通常使用 `@Singleton` 或不设作用域，由 Hilt 自动管理生命周期。
