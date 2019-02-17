package blackstone.com.soolgit.Interface

import blackstone.com.soolgit.DataClass.NaverWebRequestData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverServerInterface {

    @GET("/v1/nid/me")
    fun naveruser(@Header("Authorization") header: String): Call<NaverWebRequestData>

}