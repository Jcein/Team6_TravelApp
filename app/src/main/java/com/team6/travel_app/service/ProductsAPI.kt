package com.team6.travel_app.service

import com.team6.travel_app.model.BaseClass
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ProductsAPI {
    @GET("products?limit=100") //extension
    fun getData() : Call<BaseClass>
    @GET
    fun getCategorizedProduct(@Url text : String) : Call<BaseClass>//sınıf adı farklı Single olması gerekirdi
    @GET("Biển")
    fun getCategoryFromAPI() : Single<List<String>>
    @GET("Núi")
    fun getSmartPhonesFromAPI() : Single<BaseClass>
}