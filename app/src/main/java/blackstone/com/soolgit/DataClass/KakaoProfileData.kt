package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

class KakaoProfileData {

    @SerializedName("id")
    var id: String? = null
    @SerializedName("profile_image")
    var profile_image: String? = null
    @SerializedName("age")
    var age: String? = null
    @SerializedName("gender")
    var gender: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("birthday")
    var birthday: String? = null

}