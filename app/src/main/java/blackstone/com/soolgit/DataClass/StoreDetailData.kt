package blackstone.com.soolgit.DataClass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class StoreDetailData : Parcelable {

    @SerializedName("STORE_ID")
    var STORE_ID: String? = null
    @SerializedName("STORE_NM")
    var STORE_NM: String? = null
    @SerializedName("STORE_B_LCN")
    var STORE_B_LCN: String? = null
    @SerializedName("STORE_M_LCN")
    var Store_M_LCN: String? = null
    @SerializedName("STORE_TIME")
    var STORE_TIME: String? = null
    @SerializedName("STORE_CALL")
    var STORE_CALL: String? = null
    @SerializedName("THEME_ID")
    var THEME_ID: ArrayList<String>? = null
    @SerializedName("THEME_NM")
    var THEME_NM: ArrayList<String>? = null
    @SerializedName("IMG_ID")
    var IMG_ID: ArrayList<String>? = null
    @SerializedName("IMG_PATH")
    var IMG_PATH: ArrayList<String>? = null
    @SerializedName("STORE_CODE")
    var STORE_CODE: String? = null

}