package com.mapinspector.utils

import android.content.SharedPreferences
import com.mapinspector.utils.Constants
import com.mapinspector.utils.Constants.SharedPref.PREF_KEY_LAUNCH
import javax.inject.Inject


class SharedPreferences @Inject constructor(private val SharedPreferences: SharedPreferences) {

    fun setUserId(userId: String) = SharedPreferences.edit().putString(Constants.SharedPref.PREF_KEY_USER_ID, userId).apply()

    fun getUserId(): String? = SharedPreferences.getString(Constants.SharedPref.PREF_KEY_USER_ID, "oh")

    fun setFirstLaunch(isFirstLaunch: Boolean) = SharedPreferences.edit().putBoolean(PREF_KEY_LAUNCH, isFirstLaunch).apply()

    fun getIsFirstLaunch() = SharedPreferences.getBoolean(PREF_KEY_LAUNCH, true)
}