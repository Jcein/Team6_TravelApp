package com.team6.travel_app.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tour")
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
    @SerializedName("isChecked") val isChecked: Boolean? = null,
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
)
/*
@SerializedName("id")
@ColumnInfo(name = "id")
val id: Int? = null,
@SerializedName("description")
@ColumnInfo(name = "description")
val description: String? = null,
@SerializedName("price")
@ColumnInfo(name = "price")
val price: Int? = null,
@SerializedName("discountPercentage")
@ColumnInfo(name = "discountPercentage")
val discountPercentage: Double? = null,
@SerializedName("rating")
@ColumnInfo(name = "rating")
val rating: Double? = null,
@SerializedName("stock")
@ColumnInfo(name = "stock")
val stock: Int? = null,
@SerializedName("brand")
@ColumnInfo(name = "brand")
val brand: String? = null,
@SerializedName("category")
@ColumnInfo(name = "category")
val category: String? = null,
@SerializedName("thumbnail")
@ColumnInfo(name = "thumbnail")
val thumbnail: String? = null,
@SerializedName("images")
@ColumnInfo(name = "images")
val images: List<String>? = null,
@SerializedName("isChecked")
@ColumnInfo(name = "isChecked")
val isChecked: Int,*/
