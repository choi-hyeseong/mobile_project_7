package com.home.mindsnap.repository.user.dao

import android.content.SharedPreferences

private const val JOIN = "FIRST_JOIN"
class PreferenceUserDao(private val preferences: SharedPreferences) : UserDao {

    override fun isFirstJoined(): Boolean {
        return preferences.getBoolean(JOIN, true) //preference에 저장된 값이 없을경우 true
    }

    override fun saveVisit() {
        preferences.edit().putBoolean(JOIN, false).apply()
    }
}