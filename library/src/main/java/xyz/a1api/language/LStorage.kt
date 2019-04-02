package xyz.a1api.language

import android.content.Context
import android.support.annotation.CheckResult
import android.text.TextUtils
import android.util.Log
import android.util.LruCache
import java.lang.ref.SoftReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Created by Cat-x on 2018/7/16.
 * For C_Comic
 * Cat-x All Rights Reserved
 */
class LStorage(private val tag: String = TAG) {
    companion object {
        val SP = LStorage()
        private const val TAG = "LStorage"

        private lateinit var mContext: SoftReference<Context>
        fun init(context: Context) {
            mContext = SoftReference(context.applicationContext)
        }

        //取获取系统分配给当前应用的最大内存的1/32大小作为缓存区来缓存数据
        private var lruCache: LruCache<String, Any> =
            object : LruCache<String, Any>((Runtime.getRuntime().maxMemory() / 32).toInt()) {
                override fun sizeOf(key: String, value: Any?): Int {
                    //重写sizeOf 方法，来进行缓存的数据的大小计算，算法内部会根据该方法的返回值进行内存分配，
                    //如果放到缓存区中的内存占用超过 cacheSize ，就会删除不最近最少使用的数据
                    return when (value) {
                        null -> 1
                        //Accurately calculate consumption context，performing simulation calculations
                        is String -> value.length * 2/*value.toByteArray().size*/
                        is Boolean -> 1
                        is Int -> 4
                        is Long -> 8
                        is Float -> 4
                        is Set<*> -> value.sumBy {
                            //Unable to calculate memory size accurately，can only simulate calculations
                            if (it is String) (it.length * 2) else 8
                        }
                        else -> 100 //This should not happen
                    }

                }
            }

        /**
         * 往缓存区存数据
         */
        private fun put(key: String, value: Any?) {
            if (value == null) {
                Log.e(TAG, "can't put null to cache because key or value is null")
                return
            }
            lruCache.put(key, value)
        }

        /**
         * 取缓存区的数据
         */
        private fun get(key: String): Any? {
            if (TextUtils.isEmpty(key)) {
                Log.e(TAG, "can't get value because key is null")
                return null
            }
            return lruCache.get(key)
        }

        /**
         * 取缓存区的数据
         */
        private fun remove(key: String): Any? {
            if (TextUtils.isEmpty(key)) {
                Log.e(TAG, "can't remove value because key is null")
                return null
            }
            return lruCache.remove(key)
        }

        /**
         * 清楚缓存区数据
         */
        fun clearCache() {
            lruCache.evictAll()
        }
    }


    private fun safePut(key: String, value: Any, putData: (context: Context) -> Unit): LStorage {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "can't put value because key is empty")
        } else {
            val context = mContext.get()
            if (context != null) {
                put(key, value)
                putData(context)
                observableChange[key]?.invoke(value)
            }
        }
        return this
    }

    @Suppress("UNCHECKED_CAST")
    @CheckResult
    private fun <T> safeGet(key: String, getData: (context: Context) -> T?): T? {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "can't get value because key is empty")
        } else {
            val context = mContext.get()
            if (context != null) {
                return get(key) as T? ?: getData(context)
            }
        }
        return null
    }

    fun putString(key: String, value: String): LStorage {
        safePut(key, value) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).edit().putString(key, value).apply()
        }
        return this
    }


    fun getString(key: String, defaultValue: String = ""): String {
        return safeGet(key) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).getString(key, defaultValue)
        }!!
    }

    fun putInt(key: String, value: Int): LStorage {
        safePut(key, value) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
        }
        return this
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return safeGet(key) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).getInt(key, defaultValue)
        } ?: defaultValue
    }

    fun putBoolean(key: String, value: Boolean): LStorage {
        safePut(key, value) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()
        }
        return this
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return safeGet(key) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).getBoolean(key, defaultValue)
        } ?: defaultValue
    }

    fun putFloat(key: String, value: Float): LStorage {
        safePut(key, value) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).edit().putFloat(key, value).apply()
        }
        return this
    }


    fun getFloat(key: String, defaultValue: Float): Float {
        return safeGet(key) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).getFloat(key, defaultValue)
        } ?: defaultValue
    }

    fun putLong(key: String, value: Long): LStorage {
        safePut(key, value) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).edit().putLong(key, value).apply()
        }
        return this
    }


    fun getLong(key: String, defaultValue: Long): Long {
        return safeGet(key) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).getLong(key, defaultValue)
        } ?: defaultValue
    }

    fun putStringSet(key: String, value: Set<String>): LStorage {
        safePut(key, value) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).edit().putStringSet(key, value).apply()
        }
        return this
    }


    fun getStringSet(key: String, defaultValue: Set<String> = setOf()): Set<String> {
        return safeGet(key) {
            it.getSharedPreferences(tag, Context.MODE_PRIVATE).getStringSet(key, defaultValue)
        }!!
    }

    fun delete(key: String) {
        remove(key)
        mContext.get()?.getSharedPreferences(tag, Context.MODE_PRIVATE)?.edit()?.remove(key)?.apply()
    }

    fun preferenceString(key: String, defaultValue: String): Preference<String> {
        return Preference(this, key, defaultValue)
    }

    fun preferenceInt(key: String, defaultValue: Int = 0): Preference<Int> {
        return Preference(this, key, defaultValue)
    }

    fun preferenceBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean> {
        return Preference(this, key, defaultValue)
    }

    fun preferenceLong(key: String, defaultValue: Long = 0L): Preference<Long> {
        return Preference(this, key, defaultValue)
    }

    fun preferenceFloat(key: String, defaultValue: Float = 0F): Preference<Float> {
        return Preference(this, key, defaultValue)
    }

    fun preferenceStringSet(key: String, defaultValue: Set<String>): Preference<Set<String>> {
        return Preference(this, key, defaultValue)
    }

    fun preferenceString(
        defaultValue: String = "",
        key: String? = null
    ): ReadOnlyProperty<Nothing?, Preference<String>> {
        return Auto(defaultValue, key)
    }

    fun preferenceInt(defaultValue: Int = 0, key: String? = null): ReadOnlyProperty<Nothing?, Preference<Int>> {
        return Auto(defaultValue, key)
    }

    fun preferenceBoolean(
        defaultValue: Boolean = false,
        key: String? = null
    ): ReadOnlyProperty<Nothing?, Preference<Boolean>> {
        return Auto(defaultValue, key)
    }

    fun preferenceLong(defaultValue: Long = 0L, key: String? = null): ReadOnlyProperty<Nothing?, Preference<Long>> {
        return Auto(defaultValue, key)
    }

    fun preferenceFloat(defaultValue: Float = 0F, key: String? = null): ReadOnlyProperty<Nothing?, Preference<Float>> {
        return Auto(defaultValue, key)
    }

    fun preferenceStringSet(
        defaultValue: Set<String> = setOf(),
        key: String? = null
    ): ReadOnlyProperty<Nothing?, Preference<Set<String>>> {
        return Auto(defaultValue, key)
    }

    private inner class Auto<T>(val defaultValue: T, val key: String?) : ReadOnlyProperty<Nothing?, Preference<T>> {
        override fun getValue(thisRef: Nothing?, property: KProperty<*>): Preference<T> {
            return Preference(this@LStorage, key ?: property.name, defaultValue)
        }
    }

    private val observableChange: HashMap<String, (data: Any) -> Unit> = hashMapOf()

    class Preference<T> internal constructor(private val instance: LStorage, val key: String, val defaultValue: T) {

        @Suppress("UNCHECKED_CAST")
        fun set(value: T): Preference<T> {
            when (value) {
                is Long -> instance.putLong(key, value)
                is String -> instance.putString(key, value)
                is Int -> instance.putInt(key, value)
                is Boolean -> instance.putBoolean(key, value)
                is Float -> instance.putFloat(key, value)
                is Set<*> -> instance.putStringSet(key, value as Set<String>)
                else -> throw IllegalAccessException("This type can be saved into Preferences")
            }
            return this
        }

        @Suppress("UNCHECKED_CAST")
        @CheckResult
        fun get(): T {
            val res: Any = when (defaultValue) {
                is Long -> instance.getLong(key, defaultValue)
                is String -> instance.getString(key, defaultValue)
                is Int -> instance.getInt(key, defaultValue)
                is Boolean -> instance.getBoolean(key, defaultValue)
                is Float -> instance.getFloat(key, defaultValue)
                is Set<*> -> instance.getStringSet(key, defaultValue as Set<String>)
                else -> throw IllegalAccessException("This type can be saved into Preferences")
            }
            return res as T
        }

        fun delete(): Preference<T> {
            instance.delete(key)
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun observeAndInit(onChange: (data: T) -> Unit): Preference<T> {
            onChange(get())
            instance.observableChange[key] = onChange as (data: Any) -> Unit
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun observe(onChange: (data: T) -> Unit): Preference<T> {
            instance.observableChange[key] = onChange as (data: Any) -> Unit
            return this
        }

        fun unObserve(): Preference<T> {
            instance.observableChange.remove(key)
            return this
        }
    }

}


