package blackstone.com.soolgit.Interface

import blackstone.com.soolgit.DataClass.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserServerInterface {

    @POST("createuser")
    fun createuser(@Body userData: UserData?): Call<Void>

}