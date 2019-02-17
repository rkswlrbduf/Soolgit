package blackstone.com.soolgit.Util

import android.content.Context
import android.content.SharedPreferences

class mSharedPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val PREF_KEY_MY_EDITTEXT = "myEditText"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

//    var myEditText: String
//        get() = prefs.getString(PREF_KEY_MY_EDITTEXT, "")
//        set(value) = prefs.edit().putString(PREF_KEY_MY_EDITTEXT, value).apply()

}