//package xyz.a1api.language
//
//import android.app.Activity
//import android.os.Build
//import android.os.Bundle
//import android.support.v4.content.ContextCompat
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import kotlinx.android.synthetic.main.view_language.*
//import xyz.a1api.language.BaseLanguageActivity.Companion.getSelectLanguage
//import xyz.a1api.language.BaseLanguageActivity.Companion.loadLanguage
//import xyz.a1api.language.BaseLanguageActivity.Companion.setSelectLanguage
//import java.util.*
//
///**
// * Created by Cat-x on 2019/4/1.
// * For SwitchLanguage
// * Cat-x All Rights Reserved
// */
//class LanguageActivity : Activity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.view_language)
//        initViews()
//    }
//
//    fun initViews() {
//        english.setOnClickListener {
//            changeAppLanguage(Locale.ENGLISH, englishText, checkEnglish)
//        }
//
//        simplifiedChinese.setOnClickListener {
//            changeAppLanguage(Locale.SIMPLIFIED_CHINESE, simplifiedChineseText, checkSimplifiedChinese)
//        }
//
//        traditionalChinese.setOnClickListener {
//            changeAppLanguage(Locale.TRADITIONAL_CHINESE, traditionalChineseText, checkTraditionalChinese)
//        }
//
//        korean.setOnClickListener {
//            changeAppLanguage(Locale.KOREAN, koreanText, checkKorean)
//        }
//
//        japanese.setOnClickListener {
//            changeAppLanguage(Locale.JAPANESE, japaneseText, checkJapanese)
//        }
//        var locale = getSelectLanguage(baseContext, true)
//        if (locale == null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                locale = App.app.resources.configuration.locales.get(0)
//            } else {
//                locale = App.app.resources.configuration.locale
//            }
//        }
//        when (locale!!.language) {
//            Locale.ENGLISH.language -> {
//                showSelect(englishText, checkEnglish)
//            }
//            Locale.CHINESE.language -> {
//                if (locale.country == Locale.SIMPLIFIED_CHINESE.country) {//country/region
//                    showSelect(simplifiedChineseText, checkSimplifiedChinese)
//                } else {
//                    showSelect(traditionalChineseText, checkTraditionalChinese)
//                }
//            }
//            Locale.KOREAN.language -> {
//                showSelect(koreanText, checkKorean)
//            }
//            Locale.JAPANESE.language -> {
//                showSelect(japaneseText, checkJapanese)
//            }
//            else -> {
//                showSelect(englishText, checkEnglish)
//            }
//        }
//
//    }
//
//    private fun changeAppLanguage(locale: Locale, textView: TextView, checkImage: ImageView) {
//        invisibleSelect()
//        showSelect(textView, checkImage)
//        setSelectLanguage(locale, this)
//        loadLanguage(locale, App.app)
//        recreate()
//    }
//
//    private fun showSelect(textView: TextView, checkImage: ImageView) {
//        textView.setTextColor(ContextCompat.getColor(this, R.color.color_light_green))
//        checkImage.visibility = View.VISIBLE
//    }
//
//    private fun invisibleSelect() {
//        val color = ContextCompat.getColor(this, android.R.color.black)
//        englishText.setTextColor(color)
//        simplifiedChineseText.setTextColor(color)
//        traditionalChineseText.setTextColor(color)
//        koreanText.setTextColor(color)
//        japaneseText.setTextColor(color)
//        checkEnglish.visibility = View.INVISIBLE
//        checkSimplifiedChinese.visibility = View.INVISIBLE
//        checkTraditionalChinese.visibility = View.INVISIBLE
//        checkKorean.visibility = View.INVISIBLE
//        checkJapanese.visibility = View.INVISIBLE
//    }
//
//
//}
