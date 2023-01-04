package com.qikkle.barcodemapping.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("UserId")
    val userId: String,
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("IsSuccess")
    val isSuccess: Int,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("LocationId")
    val locationId: Int,
    @SerializedName("MobileNo")
    val mobileNo: String,
    @SerializedName("ModifiedBy")
    val modifiedBy: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("PasswordKey")
    val passwordKey: String,
    @SerializedName("RoleId")
    val roleId: String,
    @SerializedName("RoleName")
    val roleName: String,
)