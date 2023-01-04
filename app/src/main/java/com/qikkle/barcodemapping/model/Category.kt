package com.qikkle.barcodemapping.model


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("Category")
    val category: String,
    @SerializedName("CategoryId")
    val categoryId: Long,
    @SerializedName("IsSuccess")
    val isSuccess: Int,
    @SerializedName("ModifiedBy")
    val modifiedBy: String
)