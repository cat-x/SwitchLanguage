package xyz.a1api.language.app

import android.app.Application
import xyz.a1api.language.LanguageInject

/**
 * Created by Cat-x on 2019/4/1.
 * For SwitchLanguage
 * Cat-x All Rights Reserved
 */
open class App : Application() {

    companion object {
        lateinit var app: App
            private set
    }


    override fun onCreate() {
        super.onCreate()
        app = this
        LanguageInject.onAppCreate(this)

    }

}