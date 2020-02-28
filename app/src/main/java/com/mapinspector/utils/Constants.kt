package com.mapinspector.utils

object Constants {

    object Delay {
        const val SPLASH_TIME_DELAY = 3000L
    }

    object Api{
        const val BASE_URL = "https://map-inspector-070400.firebaseio.com/"
    }

    object SharedPref{
        const val PREF_KEY_USER = "user_places"
        const val PREF_KEY_USER_ID = "user_id"
        const val PREF_KEY_LAUNCH = "first_launch"
        const val PREF_KEY_PERMISSION = "permission_granted"
    }
}