package com.bogarsoft.babynames.network


import com.bogarsoft.babynames.models.responses.AppUpdateResponse
import com.bogarsoft.babynames.models.responses.BabyNameResponse
import com.bogarsoft.babynames.models.responses.NakshatraResponse
import com.bogarsoft.babynames.models.responses.RashiResponse
import com.bogarsoft.babynames.models.responses.ReligionResponse
import com.bogarsoft.babynames.utils.API
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET(API.GET_BABYNAMES_LAZY_LOADING)
    suspend fun getBabyNamesByLazyLoading(
        @Query("skip") limit: Int,@Query("alphabet") alphabet:String,@Query("gender") gender:ArrayList<String>,@Query("selected_menu") selected_menu:String,@Query("selectedid") selectedid:Int): Response<BabyNameResponse>

    @GET(API.GET_RELIGION_BY_DATA)
    suspend fun getReligionsByData(): Response<ReligionResponse>


    @GET(API.GET_RASHI)
    suspend fun getRashi(): Response<RashiResponse>

    @GET(API.GET_NAKSHATRAM)
    suspend fun getNakshatram(): Response<NakshatraResponse>

    @GET(API.GET_BABYNAMES_BY_IDS)
    suspend fun getBabyNamesByIds(@Query("ids") ids:ArrayList<Int>): Response<BabyNameResponse>

    @GET(API.GET_APP_UPDATE)
    suspend fun getAppUpdate(): Response<AppUpdateResponse>

}