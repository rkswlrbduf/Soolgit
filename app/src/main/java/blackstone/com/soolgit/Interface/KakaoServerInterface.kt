package blackstone.com.soolgit.Interface

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface KakaoServerInterface {

    @GET("/v2/user/me")
    fun kakaouser(@Header("Authorization") header: String): Call<String>

}