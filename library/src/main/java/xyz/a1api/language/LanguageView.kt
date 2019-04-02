package xyz.a1api.language

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_language.view.*
import java.util.*

/**
 * Created by Cat-x on 2019/4/1.
 * For SwitchLanguage
 * Cat-x All Rights Reserved
 */
open class LanguageView : RelativeLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater).inflate(
            R.layout.view_language,
            this
        )
        @Suppress("LeakingThis")
        initViews()
    }


    open fun initViews() {
        english.setOnClickListener {
            changeAppLanguage(Locale.ENGLISH, englishText, checkEnglish)
        }

        simplifiedChinese.setOnClickListener {
            changeAppLanguage(Locale.SIMPLIFIED_CHINESE, simplifiedChineseText, checkSimplifiedChinese)
        }

        traditionalChinese.setOnClickListener {
            changeAppLanguage(Locale.TRADITIONAL_CHINESE, traditionalChineseText, checkTraditionalChinese)
        }

        korean.setOnClickListener {
            changeAppLanguage(Locale.KOREAN, koreanText, checkKorean)
        }

        japanese.setOnClickListener {
            changeAppLanguage(Locale.JAPANESE, japaneseText, checkJapanese)
        }
        var locale = BaseLanguageActivity.getSelectLanguage(context, true)
        if (locale == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = LanguageInject.app.resources.configuration.locales.get(0)
            } else {
                locale = LanguageInject.app.resources.configuration.locale
            }
        }
        when (locale!!.language) {
            Locale.ENGLISH.language -> {
                showSelect(englishText, checkEnglish)
            }
            Locale.CHINESE.language -> {
                if (locale.country == Locale.SIMPLIFIED_CHINESE.country) {//country/region
                    showSelect(simplifiedChineseText, checkSimplifiedChinese)
                } else {
                    showSelect(traditionalChineseText, checkTraditionalChinese)
                }
            }
            Locale.KOREAN.language -> {
                showSelect(koreanText, checkKorean)
            }
            Locale.JAPANESE.language -> {
                showSelect(japaneseText, checkJapanese)
            }
            else -> {
                showSelect(englishText, checkEnglish)
            }
        }

    }

    private fun changeAppLanguage(locale: Locale, textView: TextView, checkImage: ImageView) {
        invisibleSelect()
        showSelect(textView, checkImage)
        BaseLanguageActivity.setSelectLanguage(locale, context)
        BaseLanguageActivity.loadLanguage(locale, LanguageInject.app)
        (context as? Activity)?.recreate()

    }

    private fun showSelect(textView: TextView, checkImage: ImageView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_light_green))
        checkImage.visibility = View.VISIBLE
    }

    private fun invisibleSelect() {
        val color = ContextCompat.getColor(context, android.R.color.black)
        englishText.setTextColor(color)
        simplifiedChineseText.setTextColor(color)
        traditionalChineseText.setTextColor(color)
        koreanText.setTextColor(color)
        japaneseText.setTextColor(color)
        checkEnglish.visibility = View.INVISIBLE
        checkSimplifiedChinese.visibility = View.INVISIBLE
        checkTraditionalChinese.visibility = View.INVISIBLE
        checkKorean.visibility = View.INVISIBLE
        checkJapanese.visibility = View.INVISIBLE
    }

}
