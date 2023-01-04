package com.qikkle.barcodemapping.utils

import android.content.Context
import com.google.gson.Gson
import com.qikkle.barcodemapping.utils.Constants.Companion.PREFERENCE_NAME

class PreferenceManager {

    companion object {
        fun add(context: Context, key: String, value: Any) {
            val sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            when (value) {
                is String -> {
                    editor.putString(key, value)
                }
                is Int -> {
                    editor.putInt(key, value)
                }
                is Boolean -> {
                    editor.putBoolean(key, value)
                }
                is Float -> {
                    editor.putFloat(key, value)
                }
                is Long -> {
                    editor.putLong(key, value)
                }
                else -> {
                    editor.putString(key, Gson().toJson(value))
                }
            }
            editor.apply()
        }

        fun getString(context: Context, key: String?, defaultValue: String?): String? {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(key, defaultValue)
        }

        fun getInteger(context: Context, key: String?, defaultValue: Int): Int {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(
                key,
                defaultValue
            )
        }

        fun getBoolean(context: Context, key: String?, defaultValue: Boolean): Boolean {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(
                key,
                defaultValue
            )
        }

        fun getFloat(context: Context, key: String?, defaultValue: Float): Float {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(
                key,
                defaultValue
            )
        }

        fun getLong(context: Context, key: String?, defaultValue: Long): Long {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getLong(
                key,
                defaultValue
            )
        }

        fun remove(context: Context, key: String?) {
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().remove(key)
                .apply()
        }
    }

    object Keys {
        const val USER_ID = "user_id"
        const val LOCATION_ID = "location_id"
    }
}