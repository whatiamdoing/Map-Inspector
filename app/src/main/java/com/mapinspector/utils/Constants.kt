package com.mapinspector.utils

object Constants {

    object Delay {
        const val SPLASH_TIME_DELAY = 3000L
        const val MAP_READY_DELAY = 1000L
    }

    object Api {
        const val BASE_URL = "https://map-inspector-070400.firebaseio.com/"
    }

    object SharedPref {
        const val PREF_KEY_USER = "user_places"
        const val PREF_KEY_USER_ID = "user_id"
        const val PREF_KEY_LAUNCH = "first_launch"
    }

    object Others {
        const val ZOOM_TO_CURRENT_LOCATION = 14f
        const val ZOOM_TO_SELECT_PLACE = 13f
        const val DB_NAME = "place.db"
    }
}