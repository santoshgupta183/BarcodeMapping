package com.qikkle.barcodemapping.api

import com.qikkle.barcodemapping.model.BarcodeDetailsResponse
import com.qikkle.barcodemapping.model.CreateAssetPostBody
import com.qikkle.barcodemapping.model.LoginResponse
import com.qikkle.barcodemapping.utils.APIConstants
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET(APIConstants.LOGIN)
    suspend fun loginAPI(
        @Query("UserName") username: String,
        @Query("Password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST(APIConstants.BARCODE_DETAILS)
    suspend fun getBarcodeDetails(
        @Field("SerialNo") barcode: String
    ): Response<BarcodeDetailsResponse>

    @POST(APIConstants.CREATE_ASSET)
    suspend fun createAsset(
        @Body postBody: CreateAssetPostBody
    ): Response<Void>
}