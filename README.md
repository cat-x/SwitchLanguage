## 一个使用kotlin编写的APP语言切换库

### 特性
 1. 切换应用语言
 2. 包括Application，Activity的Context的所选语言资源
 3. 语言选项会自动使用内存进行缓存，减少直接对文件的读写
------
### 原理
1. 重启Activity
2. 在Application创建时，置换成所选语言
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
    implementation 'com.github.cat-x:SwitchLanguage:0.23'
    implementation 'com.github.cat-x:LStorage:0.10'
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
#### 切换语言
> 目前支持简体中文，繁体中文，日语，韩语，英语的切换
* 你只要在布局中引入LanguageView即可
~~~xml
    <xyz.a1api.language.LanguageView
            android:paddingLeft="16dp"
            android:paddingRight="16dp"
            android:layout_below="@id/titleTextView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

    </xyz.a1api.language.LanguageView>
~~~
* 通过函数api切换
~~~kotlin
BaseLanguageActivity.getSelectLanguageForWeb()//获取当前App语言，用于网络api调用
BaseLanguageActivity.getSelectLanguage()//获取当前App语言的Locale
BaseLanguageActivity.setSelectLanguage()//设置语言
BaseLanguageActivity.loadLanguage()//载入选中语言的resources
~~~
#### 多语言界面
* 基于继承Activity的方式使用
~~~kotlin
class MainActivity : BaseLanguageActivity{
//...
}
~~~
* 不继承Activity,使用LanguageInject来切换
~~~kotlin
open class Main2Activity : AppCompatActivity() {
    @Suppress("LeakingThis")
    open var languageInject: LanguageInject? = object : LanguageInject(this@BaseLanguageActivity) {
        override fun isLanguageActivity(activity: Activity): Boolean {
            return this@BaseLanguageActivity.isLanguageActivity(activity)
        }
    }

   //本方法主要是判断当前Activity是不是语言切换的页面，如果是的，你需要返回true
    open fun isLanguageActivity(activity: Activity): Boolean {
        return false
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(languageInject?.attachBaseContext(newBase))
    }

    override fun onResume() {
        super.onResume()
        languageInject?.onResume()
    }
}
~~~
------
### 演示图片
![图片](./img/demo.gif)

