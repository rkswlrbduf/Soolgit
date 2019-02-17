package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class HotPlaceStoreData {

    @SerializedName("STORE_ID")
    var StoreID: String? = null
    @SerializedName("STORE_NM")
    var StoreName: String? = null
    @SerializedName("THEME_NM")
    var StoreTheme: ArrayList<String>? = null
    @SerializedName("STORE_IMG")
    var StoreImg: String? = null

}