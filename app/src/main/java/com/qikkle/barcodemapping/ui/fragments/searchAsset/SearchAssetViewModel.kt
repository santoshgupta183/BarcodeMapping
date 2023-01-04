package com.qikkle.barcodemapping.ui.fragments.searchAsset

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qikkle.barcodemapping.api.BarcodeMappingRepository
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.model.BarcodeDetailsResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchAssetViewModel(
    application: Application
): AndroidViewModel(application) {

    private val repo by lazy { BarcodeMappingRepository.instance }
    private val searchResult: MutableLiveData<Resource<BarcodeDetailsResponse>> = MutableLiveData()

    fun getSearchResult(): LiveData<Resource<BarcodeDetailsResponse>> = searchResult;

    fun getBarcodeDetails(barcode: String) = viewModelScope.launch {
        searchResult.postValue(Resource.Loading())
        val barcodeDetails = async {
            repo.getBarcodeDetails(barcode)
        }.await()

        searchResult.postValue(handleResponse(barcodeDetails))
    }

    private fun handleResponse(response: Response<BarcodeDetailsResponse>) : Resource<BarcodeDetailsResponse> {
        if (response.isSuccessful) {
            return if(response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error fetching data!!", null)
            }
        }
        return Resource.Error(response.message(), null)
    }
}