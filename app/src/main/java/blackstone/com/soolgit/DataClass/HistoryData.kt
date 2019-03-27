package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class HistoryData {

    @SerializedName("STORE_NM")
    var STORE_NM: String? = null
    @SerializedName("STORE_M_LCN")
    var STORE_M_LCN: String? = null
    @SerializedName("SERVICE_NM")
    var SERVICE_NM: String? = null
    @SerializedName("SERVICE_COST")
    var SERVICE_COST: String? = null
    @SerializedName("ORDER_DT")
    var ORDER_DT: String? = null

}