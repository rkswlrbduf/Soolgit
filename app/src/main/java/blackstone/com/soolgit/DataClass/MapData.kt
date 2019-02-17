package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class MapData {

    @SerializedName("STORE_ID")
    var StoreID: Int? = null
    @SerializedName("STORE_NM")
    var StoreName: String? = null
    @SerializedName("STORE_CALL")
    var StoreCall: String? = null
    @SerializedName("STORE_B_LCN")
    var StoreBLocation: String? = null
    @SerializedName("STORE_POINT_X")
    var StorePointX: Double? = null
    @SerializedName("STORE_POINT_Y")
    var StorePointY: Double? = null

}