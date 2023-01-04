package com.qikkle.barcodemapping.model

import com.google.gson.annotations.SerializedName
import com.qikkle.barcodemapping.utils.Constants

data class LoginResponse(
    @SerializedName("Status")
    val status: String,
    @SerializedName("ErrorText")
    val errorText: String,
    @SerializedName("UserId")
    val userId: String,
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("locationId")
    val locationId: Long
) {
    fun isSuccess(): Boolean{
        return status.equals(Constants.LOGIN_STATUS_SUCCESS, true)
    }
}
