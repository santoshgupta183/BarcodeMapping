package com.qikkle.barcodemapping.model


import com.google.gson.annotations.SerializedName

data class CreateAssetResponse(
    @SerializedName("ErrorText")
    val errorText: String,
    @SerializedName("locationId")
    val locationId: Int,
    @SerializedName("Status")
    val status: String,
    @SerializedName("UserId")
    val userId: String,
    @SerializedName("userName")
    val userName: String
)