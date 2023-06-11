package com.bogarsoft.babynames.network


import com.bogarsoft.babynames.models.responses.BabyNameResponse
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
        @Query("skip") limit: Int
    ): Response<BabyNameResponse>

}