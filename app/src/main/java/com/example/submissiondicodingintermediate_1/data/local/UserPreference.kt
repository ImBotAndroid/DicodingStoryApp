package com.example.submissiondicodingintermediate_1.data.local

import android.content.Context
import com.example.submissiondicodingintermediate_1.data.response.LoginResult

class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: LoginResult) {
        val editor = preferences.edit()
        editor.putString(USER_ID, value.userId)
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        return LoginResult(
            preferences.getString(USER_ID, "").toString(),
            preferences.getString(NAME, "").toString(),
            preferences.getString(TOKEN, "").toString()
        )
    }

    fun deleteUser(){
        val editor = preferences.edit()
        editor.putString(USER_ID, "")
        editor.putString(NAME, "")
        editor.putString(TOKEN, "")
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val USER_ID = "user_id"
        private const val TOKEN = "token"

        @Volatile
        private var instance: UserPreference? = null

        fun getInstance(context: Context): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference(context)
            }
        }
    }
}