package blackstone.com.soolgit.DataClass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HotPlaceStoreData(
        @SerializedName("STORE_ID") var StoreID: String? = null,
        @SerializedName("STORE_NM") var StoreName: String? = null,
        @SerializedName("THEME_NM") var StoreTheme: ArrayList<String>? = null,
        @SerializedName("STORE_IMG") var StoreImg: String? = null) : Parcelable