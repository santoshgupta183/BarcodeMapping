package com.qikkle.barcodemapping.api

import com.qikkle.barcodemapping.model.*
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
    ): Response<CreateAssetResponse>

    @GET(APIConstants.GET_CATEGORY)
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET(APIConstants.GET_PRODUCT)
    suspend fun getProducts (
        @Query("Category") categoryId: Long,
    ): Response<ProductsResponse>

    @GET(APIConstants.GET_RECEIVERS)
    suspend fun getReceivers (
        @Query("LocationId") locationId: Int,
    ): Response<ReceiversResponse>
}