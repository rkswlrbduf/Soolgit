package blackstone.com.soolgit.DataClass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ThemeData(
        @SerializedName("THEME_ID")
        val THEME_ID: Int,
        @SerializedName("THEME_NM")
        val THEME_NM: String,
        @SerializedName("THEME_IMG")
        val THEME_IMG: String) : Parcelable