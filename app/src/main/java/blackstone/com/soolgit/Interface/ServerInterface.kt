package blackstone.com.soolgit.Interface

import blackstone.com.soolgit.DataClass.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerInterface {

    @GET("hotplace")
    fun hotplacestore(): Call<ArrayList<HotPlaceStoreData>>

    @GET("recommend")
    fun recommend(): Call<ArrayList<HotPlaceStoreData>>

    @GET("concept")
    fun concept(): Call<ArrayList<ConceptData>>

    @GET("dong")
    fun dong(): Call<List<DongData>>

    @GET("store")
    fun store(): Call<ArrayList<StoreData>>

    @POST("storedong")
    fun storedong(@Query("pointX") PointX: Double?, @Query("pointY") PointY: Double?, @Query("categoryArray") categoryIntArray: String?, @Query("themeArray") themeStringArray: String?): Call<ArrayList<StoreData>>

    @GET("map")
    fun map(@Query("dy") dy: Double?,
            @Query("lx") lx: Double?,
            @Query("ty") ty: Double?,
            @Query("rx") rx: Double?): Call<ArrayList<StoreData>>

    @GET("search")
    fun search(@Query("text") string: String?): Call<ArrayList<StoreData>>

    @GET("storedetail")
    fun storedetail(@Query("id") string: String?): Call<StoreDetailData>

    @GET("storenoimagemenu")
    fun storenoimagemenu(@Query("id") string: String?): Call<ArrayList<StoreNoImageMenuData>>

    @GET("storeimagemenu")
    fun storeimagemenu(@Query("id") string: String?): Call<ArrayList<StoreImageMenuData>>

    @GET("storeplace")
    fun storeplace(@Query("id") string: String?): Call<ArrayList<StorePlaceData>>

    @GET("storeservice")
    fun storeservice(@Query("id") string: String?): Call<ArrayList<StoreServiceData>>

    @GET("notice")
    fun notice(): Call<ArrayList<NoticeData>>

    @GET("orderhistory")
    fun orderhistory(@Query("userid") userID: String): Call<ArrayList<HistoryData>>

    @GET("category")
    fun category(): Call<ArrayList<CategoryData>>

    @GET("theme")
    fun theme(): Call<ArrayList<ThemeData>>

    @GET("ZZIM")
    fun zzim(@Query("storeID") storeID: String, @Query("userID") userID: String, @Query("checked") checked: Boolean): Call<Void>

    @POST("servicecomplete")
    fun servicecomplete(@Body serviceCompleteData: ServiceCompleteData?): Call<Void>

    @GET("servicecheck")
    fun servicecheck(@Query("userid") userID: String, @Query("storeid") storeID: String): Call<String>

    @GET("logout")
    fun logout(@Query("userid") userID: String): Call<LogoutTypeData>

}