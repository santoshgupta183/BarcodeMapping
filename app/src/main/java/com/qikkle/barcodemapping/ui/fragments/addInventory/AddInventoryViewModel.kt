package com.qikkle.barcodemapping.ui.fragments.addInventory

import android.app.Application
import android.content.BroadcastReceiver
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qikkle.barcodemapping.api.BarcodeMappingRepository
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.model.CreateAssetPostBody
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class AddInventoryViewModel(
    application: Application
): AndroidViewModel(application) {

    private val repo by lazy { BarcodeMappingRepository.instance }
    private val saveBtnStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val createAssetResult: MutableLiveData<Resource<Void>> = MutableLiveData()

    fun getCreateAssetResult(): LiveData<Resource<Void>> = createAssetResult;

    fun getSaveBtnStatus(): LiveData<Boolean> = saveBtnStatus

    fun onDataChange(
        date: String,
        poNumber: String,
        category: String,
        product: String,
        make: String,
        receiver: String,
        barcode: String
    ){
        saveBtnStatus.postValue(date.isNotEmpty() && poNumber.isNotEmpty()
                && category.isNotEmpty() && product.isNotEmpty() && make.isNotEmpty()
                && receiver.isNotEmpty() && barcode.isNotEmpty() && !barcode.equals("NA", true))
    }

    fun saveAsset(
        date: String,
        poNumber: String,
        category: String,
        product: String,
        make: String,
        receiver: String,
        barcode: String
    ) = viewModelScope.launch {
        createAssetResult.postValue(Resource.Loading())

        val postBody = CreateAssetPostBody(
            deliveryDate = date,
            pONo = poNumber,
            categoryId = category.toInt(),
            productId = product.toInt(),
            make = make,
            serialNo = barcode,
            receiverName = receiver
        )

        val barcodeDetails = async {
            repo.createAsset(postBody)
        }.await()

        createAssetResult.postValue(handleResponse(barcodeDetails))
    }

    private fun handleResponse(response: Response<Void>) : Resource<Void> {
        if (response.isSuccessful) {
            return if(response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error saving data!!", null)
            }
        }
        return Resource.Error(response.message(), null)
    }
}