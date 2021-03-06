package xyz.a1api.language

import android.app.Activity
import android.app.Application
import android.content.Context
import xyz.a1api.tools.LStorage
import java.util.*

/**
 * Created by Cat-x on 2019/4/1.
 * For SwitchLanguage
 * Cat-x All Rights Reserved
 */
open class LanguageInject(open val activity: Activity) {

    var useLocale: Locale? = null
    open fun attachBaseContext(newBase: Context?): Context? {
        useLocale = BaseLanguageActivity.getSelectLanguage(newBase, true)
        return BaseLanguageActivity.loadLanguage(useLocale, newBase)
    }


    open fun onResume() {
        val selectLanguage = BaseLanguageActivity.getSelectLanguage(activity, true)
        if (selectLanguage != null) {
            if (useLocale != selectLanguage && !isLanguageActivity(activity)) {
                activity.recreate()
            }
        }
    }

    /**
     * 判断当前Activity是不是语言切换的页面，如果是，需要返回true
     */
    open fun isLanguageActivity(activity: Activity): Boolean {
        return false
    }

    companion object {
        lateinit var app: Application
        fun onAppCreate(app: Application) {
            this.app = app
            LStorage.init(app)
            BaseLanguageActivity.loadLanguage(BaseLanguageActivity.getSelectLanguage(app, false), app)
        }


    }
}