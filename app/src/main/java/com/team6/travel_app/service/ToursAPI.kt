package com.team6.travel_app.service

import com.team6.travel_app.model.BaseClass
import com.team6.travel_app.model.Tour
import com.team6.travel_app.model.TourBaseClass
import com.team6.travel_app.model.TourP
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Query
import retrofit2.http.Url

interface ToursAPI {
    @GET("tours") //extension
    fun getData(
        @Query("cus_id") cus_id: String,
    ) : Call<TourBaseClass>

    @POST("tours")
    fun createPost(
        @Body tourp: TourP
    ):Call<TourBaseClass>
}