package com.qikkle.barcodemapping.model


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("IsSuccess")
    val isSuccess: Int,
    @SerializedName("ModifiedBy")
    val modifiedBy: String,
    @SerializedName("Product")
    val product: String,
    @SerializedName("ProductId")
    val productId: Long
)