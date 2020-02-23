package com.mapinspector.utils

import android.content.Context
import com.mapinspector.utils.SharedPrefsHelper.Companion.PREF_KEY_ID
import com.mapinspector.utils.SharedPrefsHelper.Companion.PREF_KEY_LAUNCH
import com.mapinspector.utils.SharedPrefsHelper.Companion.PREF_KEY_USER_PLACE_NUMBER

fun getUserId(context: Context): String? {
    return context.getSharedPreferences(SharedPrefsHelper.PREF_KEY_USER, Context.MODE_PRIVATE)
        .getString(PREF_KEY_ID, "0")
}

fun setUserId(context: Context, userId: String){
    context.getSharedPreferences(SharedPrefsHelper.PREF_KEY_USER, Context.MODE_PRIVATE)
        .edit()
        .putString(PREF_KEY_ID, userId)
        .apply()
}

fun getIsFirstLaunch(context: Context): Boolean{
    return context.getSharedPreferences(SharedPrefsHelper.PREF_KEY_USER, Context.MODE_PRIVATE)
        .getBoolean(PREF_KEY_LAUNCH, true)
}

fun setFirstLaunch(context: Context, isFirstLaunch: Boolean){
    context.getSharedPreferences(SharedPrefsHelper.PREF_KEY_USER, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(PREF_KEY_LAUNCH, isFirstLaunch)
        .apply()
}

fun getPlaceNumber(context: Context): Int{
    return context.getSharedPreferences(SharedPrefsHelper.PREF_KEY_USER, Context.MODE_PRIVATE)
        .getInt(PREF_KEY_USER_PLACE_NUMBER, 1)
}

fun setPlaceNumber(context: Context, placeNumber: Int){
    context.getSharedPreferences(SharedPrefsHelper.PREF_KEY_USER, Context.MODE_PRIVATE)
        .edit()
        .putInt(PREF_KEY_USER_PLACE_NUMBER, placeNumber)
        .apply()
}

class SharedPrefsHelper{
    companion object {
        const val PREF_KEY_USER = "user_places"
        const val PREF_KEY_ID = "user_id"
        const val PREF_KEY_USER_PLACE_NUMBER = "place_number"
        const val PREF_KEY_LAUNCH = "first_launch"
    }
}