package com.team6.travel_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comment")
data class Comment(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("comment_text") val commentText: String? = null,
    @SerializedName("product_id") val productId: Int? = null,
    @SerializedName("cust_id") val custId: String? = null,
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
    )