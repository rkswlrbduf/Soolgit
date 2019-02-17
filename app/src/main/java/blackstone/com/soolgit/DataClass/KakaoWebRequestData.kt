package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class KakaoWebRequestData {

    @SerializedName("resultcode")
    var resultcode: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("response")
    var response: NaverProfileData? = null

}