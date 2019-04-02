package xyz.a1api.language.app

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import xyz.a1api.language.BaseLanguageActivity

class MainActivity : BaseLanguageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switchButton.setOnClickListener { startActivity(Intent(this, LanguageActivity::class.java)) }
    }
}
