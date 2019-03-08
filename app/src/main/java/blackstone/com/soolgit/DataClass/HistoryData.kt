package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class HistoryData {

    @SerializedName("STORE_NM")
    var STORE_NM: String? = null
    @SerializedName("STORE_M_LCN")
    var STORE_M_LCN: String? = null
    @SerializedName("MENU_NM")
    var MENU_NM: String? = null
    @SerializedName("MENU_COST")
    var MENU_COST: String? = null
    @SerializedName("HIS_DT")
    var HIS_DT: String? = null

}