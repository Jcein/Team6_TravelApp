package com.team6.travel_app.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "customer")
data class Customer  (
    @SerializedName("id") val id: String? = null,//named as productId, productName... and productList --> products
    @SerializedName("password") val password: String? = null,
    @SerializedName("cus_name") val cusName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("address") val address: String? = null,
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
)