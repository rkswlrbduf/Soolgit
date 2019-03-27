package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

data class LogoutTypeData(
        @SerializedName("USER_TYPE")
        var USER_TYPE: String? = null
)