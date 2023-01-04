package com.qikkle.barcodemapping.api

import com.qikkle.barcodemapping.model.CreateAssetPostBody
import com.qikkle.barcodemapping.model.LoginResponse
import retrofit2.Response

class BarcodeMappingRepository {

    companion object {
        val instance: BarcodeMappingRepository by lazy { BarcodeMappingRepository() }
    }

    suspend fun login(username: String, password: String): Response<LoginResponse> {
        return ApiClient.api.loginAPI(username, password)
    }

    suspend fun getBarcodeDetails(barcode:String) =
        ApiClient.api.getBarcodeDetails(barcode)

    suspend fun createAsset(createAssetPostBody: CreateAssetPostBody) =
        ApiClient.api.createAsset(createAssetPostBody)

}

