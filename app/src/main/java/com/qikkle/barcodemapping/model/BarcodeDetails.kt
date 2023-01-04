package com.qikkle.barcodemapping.model


import com.google.gson.annotations.SerializedName

data class BarcodeDetails(
    @SerializedName("Category")
    val category: String,
    @SerializedName("DeliveryDate")
    val deliveryDate: String,
    @SerializedName("Make")
    val make: String,
    @SerializedName("PONo")
    val pONo: String,
    @SerializedName("Product")
    val product: String,
    @SerializedName("Qty")
    val qty: String,
    @SerializedName("SerialNo")
    val serialNo: String
)