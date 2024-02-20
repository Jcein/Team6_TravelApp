package com.team6.travel_app.service

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.team6.travel_app.model.Comments
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Body

interface CommentAPI {
    @GET
    fun getComment(@Url text : String) : Call<Comments>
    @GET("comments")
    fun getComments() : Response<List<Comments>>
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(it: List<Comments>)
    @POST("comment")
    fun updateComments(@Body comments: List<Comments>): Call<Void>
}
