package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class StoreData {

    @SerializedName("STORE_ID")
    var StoreID: Int? = null
    @SerializedName("STORE_IMG")
    var StoreImage: String? = null
    @SerializedName("STORE_NM")
    var StoreName: String? = null
    @SerializedName("STORE_M_LCN")
    var StoreMLocation: String? = null
    @SerializedName("STORE_B_LCN")
    var StoreBLocation: String? = null
    @SerializedName("THEME_NM")
    var StoreTheme: ArrayList<String>? = null
    @SerializedName("STORE_HEART")
    var StoreHeart: String? = null
    @SerializedName("SERVICE_NM")
    var StoreService: String? = null
    @SerializedName("STORE_CALL")
    var StoreCall: String? = null
    @SerializedName("STORE_POINT_X")
    var StorePointX: Double? = null
    @SerializedName("STORE_POINT_Y")
    var StorePointY: Double? = null
    @SerializedName("STORE_MENU_LIST")
    var StoreMenuList: ArrayList<String>? = null
    @SerializedName("COUNT")
    var StoreZzimCount: String? = null

}