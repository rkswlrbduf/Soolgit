package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class StoreServiceData {

    @SerializedName("SERVICE_ID")
    var SERVICE_ID: String? = null
    @SerializedName("SERVICE_NM")
    var SERVICE_NM: String? = null
    @SerializedName("TAG_NM")
    var TAG_NM: ArrayList<String>? = null
    @SerializedName("SERVICE_IMG")
    var SERVICE_IMG: String? = null

}