package com.team6.travel_app.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class CusBaseClass(
    @SerializedName("customer") val customer: List<Customer>? = null,
    @SerializedName("total") val total: Int? = null,
    @SerializedName("skip") val skip: Int? = null,
    @SerializedName("limit") val limit: Int? = null
) : Parcelable

@Parcelize
data class Customer(
    @SerializedName("id") val id: String? = null,//named as productId, productName... and productList --> products
    @SerializedName("password") val password: String? = null,
    @SerializedName("cus_name") val cusName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("address") val address: String? = null,
) : Parcelable
