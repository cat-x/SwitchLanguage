package xyz.a1api.language.app

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_language.*
import xyz.a1api.language.BaseLanguageActivity

class LanguageActivity : BaseLanguageActivity() {

    override fun isLanguageActivity(activity: Activity): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        backButton.setOnClickListener { onBackPressed() }
    }
}
