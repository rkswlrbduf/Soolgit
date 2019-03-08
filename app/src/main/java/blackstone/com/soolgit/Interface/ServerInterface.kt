package blackstone.com.soolgit.Interface

import blackstone.com.soolgit.DataClass.*
import retrofit2.Call
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
    fun storedong(@Query("pointX") PointX: Double?, @Query("pointY") PointY: Double?): Call<ArrayList<StoreData>>

    @GET("map")
    fun map(@Query("dy") dy: Double?,
            @Query("lx") lx: Double?,
            @Query("ty") ty: Double?,
            @Query("rx") rx: Double?): Call<List<MapData>>

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

    @GET("history")
    fun history(): Call<ArrayList<HistoryData>>

    @GET("category")
    fun category(): Call<ArrayList<CategoryData>>

    @GET("theme")
    fun theme(): Call<ArrayList<ThemeData>>

}