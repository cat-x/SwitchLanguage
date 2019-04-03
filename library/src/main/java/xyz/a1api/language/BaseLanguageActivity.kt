package xyz.a1api.language

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.support.v7.app.AppCompatActivity
import xyz.a1api.tools.LStorage
import java.util.*


/**
 * Created by Cat-x on 2019/4/1.
 * For SwitchLanguage
 * Cat-x All Rights Reserved
 */
@SuppressLint("Registered")
open class BaseLanguageActivity : AppCompatActivity() {
    @Suppress("LeakingThis")
    open var languageInject: LanguageInject? = object : LanguageInject(this@BaseLanguageActivity) {
        override fun isLanguageActivity(activity: Activity): Boolean {
            return this@BaseLanguageActivity.isLanguageActivity(activity)
        }
    }

    /**
     * 判断当前Activity是不是语言切换的页面，如果是，需要返回true
     */
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

    override fun onDestroy() {
        super.onDestroy()
        languageInject = null
    }

    override fun recreate() {
        handFragment()
        super.recreate()
    }

    open fun handFragment() {
        try {//避免重启太快 恢复
            val fragments = supportFragmentManager.fragments
            if (fragments.isNotEmpty()) {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                for (fragment in fragments) {
                    val childFragmentManager = fragment.childFragmentManager
                    val childFragments = childFragmentManager.fragments
                    if (childFragments.isNotEmpty()) {
                        val childFragmentTransaction = childFragmentManager.beginTransaction()
                        for (childFragment in childFragments) {
                            childFragmentTransaction.remove(childFragment)
                        }
                        childFragmentTransaction.commitAllowingStateLoss()
                    }
                    fragmentTransaction.remove(fragment)
                }
                fragmentTransaction.commitAllowingStateLoss()
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        //        var locale: Locale? = null
        const val LANGUAGE_CONFIG = "languageConfig"
        const val LANGUAGE = "language"
        const val COUNTRY = "country"

        /**
         * 获取当前App语言，用于网络api调用，会返回“语言-国家”的形式;如果没有国家，则直接返回“语言”
         */
        fun getSelectLanguageForWeb(context: Context, isUseIOS: Boolean = false): String? {
            val language = getSelectLanguage(context, false)
            return if (language?.country.isNullOrEmpty()) language?.language else {
                if (isUseIOS) {
                    if (language?.language == "zh") {
                        return if (language.country == "CN") {
                            language.language + "-Hans"
                        } else {
                            language.language + "-Hant"
                        }
                    }
                }
                language?.language + "-" + language?.country
            }
        }

        /**
         * 获取当前App语言的Locale
         */
        fun getSelectLanguage(newBase: Context?, canNull: Boolean = false): Locale? {
            if (newBase == null) {
                return null
            }


            val language = LStorage.SP.getString(LANGUAGE, "")
            val country = LStorage.SP.getString(COUNTRY, "")
            if (language.isBlank()) {
                if (canNull) {
                    return null
                }
                //如果不返回这个默认值进行设置的话，后面语言发生更改的情况下，locales会自动跟随设置的改变，从而导致isSameWithSetting无法知道语言已经不一致
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newBase.resources.configuration.locales[0]
                } else {
                    newBase.resources.configuration.locale
                }
            }
            return Locale(language, country)
        }

        /**
         * 设置语言
         */
        fun setSelectLanguage(locale: Locale, newBase: Context) {
            LStorage.SP.putString(LANGUAGE, locale.language)
            LStorage.SP.putString(COUNTRY, locale.country)
//            this.locale = locale
        }

        /**
         * 载入选中语言的resources
         */
        fun loadLanguage(locale: Locale?, newBase: Context?): Context? {
            if (locale != null && newBase != null) {
                val resources = newBase.resources
                val metrics = resources.displayMetrics
                val configuration = resources.configuration

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLocale(locale)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        configuration.locales = LocaleList(locale)
                    }
                    resources.updateConfiguration(configuration, metrics)
                    return newBase.createConfigurationContext(configuration)
                } else {
                    configuration.locale = locale
                    resources.updateConfiguration(configuration, metrics)
                    return newBase
                }
            }
            return newBase
        }

//        fun isSameWithSetting(context: Context): Boolean {
//            val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                context.resources.configuration.locales[0]
//            } else {
//                context.resources.configuration.locale
//            }
//            if (locale == null) {//如果没有读取过配置
//                locale = getSelectLanguage(context, true)
//                if (locale == null) {//如果读取的配置也为空
//                    locale = current//则将现有的系统默认地区设置到配置中
//                }
//            }
//
//            return current == locale
//        }
    }
}