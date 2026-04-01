# NaviPlayer 导航架构方案总结

本文档旨在总结 NaviPlayer 项目当前采用的导航方案，以便于团队成员理解和后续维护。

## 1. 核心技术选型
项目采用 **Jetpack Navigation Component (Fragment-based)** 作为全应用路由框架。

## 2. 模块化设计
导航逻辑遵循项目的多模块架构进行解耦：

### A. 核心导航模块 (`:core:navigation`)
*   **定位**：底层依赖模块，不包含具体的业务页面。
*   **职责**：
    *   定义通用的导航配置（如 `CommonNavOptions`）。
    *   封装标准的进出场动画（`slide_in` / `slide_out` 等）。
    *   作为其他功能模块导航能力的提供方。

### B. 功能模块 (`:feature:xxx:impl`)
*   **定位**：业务实现模块。
*   **职责**：
    *   持有模块内部的导航图（如 `login_graph.xml`）。
    *   定义模块内的 `Fragment` 节点及其跳转逻辑。
    *   通过嵌套导航（Nested Graphs）被集成到主图中。

### C. 壳工程模块 (`:app`)
*   **定位**：应用入口与集成层。
*   **职责**：
    *   持有根导航图 `main_nav_graph.xml`。
    *   通过 `<include>` 标签集成各功能的导航图。
    *   配置 `MainActivity` 中的 `NavHostFragment` 容器。

## 3. 关键特性实现

### 统一动画管理
通过 `CommonNavOptions` 对象，在代码中统一管理 Fragment 切换动画：
```kotlin
val slideInOptions = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}
```

### 导航图集成结构
```xml
<!-- main_nav_graph.xml -->
<navigation ... app:startDestination="@id/login_history_dest">
    <!-- 包含功能模块导航图 -->
    <include app:graph="@navigation/login_graph" />
    
    <!-- 本地定义或跨模块定义的节点 -->
    <fragment android:id="@+id/login_history_dest" ... />
</navigation>
```

## 4. 后续演进方向
1.  **类型安全**：引入 **Safe Args** 插件，替代 Bundle 传参，增强类型检查。
2.  **解耦跳转**：在 `:feature:xxx:api` 层定义导航接口，利用依赖注入（Hilt/Koin）在各模块间实现透明跳转，消除模块间的硬依赖。
3.  **深层链接**：利用 Navigation 的 Deep Link 功能支持外部拉起和模块化跳转。

---
*最后更新时间：2026-03-31*
