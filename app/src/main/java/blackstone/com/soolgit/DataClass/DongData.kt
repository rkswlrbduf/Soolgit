package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class DongData {

    @SerializedName("DONG_ID")
    var DongID: Int? = null
    @SerializedName("DONG_NM")
    var DongName: String? = null
    @SerializedName("DONG_IMG")
    var DongImage: String? = null
    var DongChecked: Boolean? = false

}