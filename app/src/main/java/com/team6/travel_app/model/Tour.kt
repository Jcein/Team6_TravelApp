package com.team6.travel_app.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class TourBaseClass(
    @SerializedName("tours") val tours: List<Tour>? = null,
    @SerializedName("total") val total: Int? = null,
    @SerializedName("skip") val skip: Int? = null,
    @SerializedName("limit") val limit: Int? = null
) : Parcelable

@Parcelize
data class Tour(
    @SerializedName("id") val id: Int? = null,//named as productId, productName... and productList --> products
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: Int? = null,
    @SerializedName("quantity") val quantity: Int? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("discountPercentage") val discountPercentage: Double? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("registrationDate") val registrationDate: String? = null,
    @SerializedName("images") val images: String? = null,
    @SerializedName("isChecked") val isChecked: Boolean? = null
) : Parcelable

@Parcelize
data class TourP(
    @SerializedName("quantity") val quantity: Int? = null,
    @SerializedName("registration_date") val registrationDate: String? = null,
    @SerializedName("cus_id") val cusId: String? = null,
    @SerializedName("product_id") val productId: Int? = null,
    @SerializedName("isChecked") val isChecked: Int? = null
) : Parcelable