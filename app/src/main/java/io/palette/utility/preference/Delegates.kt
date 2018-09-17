package io.palette.utility.preference

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun bindBoolean(key: String, defValue: Boolean = false):
        ReadWriteProperty<BasePreferences, Boolean> = PrefFieldDelegate(Boolean::class, key, defValue)

fun bindInt(key: String, defValue: Int = 0):
        ReadWriteProperty<BasePreferences, Int> = PrefFieldDelegate(Int::class, key, defValue)

fun bindLong(key: String, defValue: Long = 0):
        ReadWriteProperty<BasePreferences, Long> = PrefFieldDelegate(Long::class, key, defValue)

fun bindFloat(key: String, defValue: Float = 0F):
        ReadWriteProperty<BasePreferences, Float> = PrefFieldDelegate(Float::class, key, defValue)

fun bindString(key: String, defValue: String = ""):
        ReadWriteProperty<BasePreferences, String> = PrefFieldDelegate(String::class, key, defValue)

inline fun <reified T : Any> bindJson(key: String, defValue: T):
        ReadWriteProperty<BasePreferences, T> = PrefFieldDelegate(T::class, key, defValue)

open class PrefFieldDelegate<T : Any>(
        private val clazz: KClass<T>,
        private val key: String,
        private val defValue: T
) : ReadWriteProperty<BasePreferences, T> {
    override fun getValue(thisRef: BasePreferences, property: KProperty<*>): T {
        return thisRef.get(clazz, key, defValue)
    }

    override fun setValue(thisRef: BasePreferences, property: KProperty<*>, value: T) {
        thisRef.put(clazz, key, value)
    }

}