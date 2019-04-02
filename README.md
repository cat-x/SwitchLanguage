## 一个使用kotlin编写的APP语言切换库

### 特性
 
------
### 原理

------
### 引入架包

In your root path  `build.gradle`:

```groovy
allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }
```
In your Application Dir `build.gradle`:
```groovy
dependencies {
    implementation 'com.github.cat-x:SwitchLanguage:0.1'
}
```
------
### 使用方法
1. 在使用前你应该进行初始化
> 你需要在Application 中初始化，只需要调用1次即可
~~~kotlin
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LanguageInject.onAppCreate(this)
    }
}
~~~
2. 函数调用


------
