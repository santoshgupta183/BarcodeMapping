package com.qikkle.barcodemapping.ui.fragments.addInventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qikkle.barcodemapping.api.BarcodeMappingRepository
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.model.*
import com.qikkle.barcodemapping.utils.PreferenceManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class AddInventoryViewModel(
    val app: Application
): AndroidViewModel(app) {

    private val repo by lazy { BarcodeMappingRepository.instance }
    private val saveBtnStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val categoriesLiveData: MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    private val productsLiveData: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    private val receiversLiveData: MutableLiveData<Resource<List<Receiver>>> = MutableLiveData()
    private val createAssetResult: MutableLiveData<Resource<CreateAssetResponse>> = MutableLiveData()

    private val categories = mutableListOf<Category>()
    private val products = mutableListOf<Product>()
    private val receivers = mutableListOf<Receiver>()

    private var selectedCategory: Category? = null
    private var selectedProduct: Product? = null
    private var selectedReceiver: Receiver? = null

    init {
        fetchCategories()
    }

    fun getCategoriesLiveData(): LiveData<Resource<List<Category>>> = categoriesLiveData

    fun getProductsLiveData(): LiveData<Resource<List<Product>>> = productsLiveData

    fun getReceiversLiveData(): LiveData<Resource<List<Receiver>>> = receiversLiveData

    fun getCreateAssetResult(): LiveData<Resource<CreateAssetResponse>> = createAssetResult;

    fun getSaveBtnStatus(): LiveData<Boolean> = saveBtnStatus

    private fun fetchCategories()= viewModelScope.launch {
        categoriesLiveData.postValue(Resource.Loading())
        val response = repo.getCategories()
        categoriesLiveData.postValue(handleCategoriesResponse(response))
        fetchReceivers()
    }

    private fun fetchReceivers()= viewModelScope.launch {
        receiversLiveData.postValue(Resource.Loading())
        val response = repo.getReceivers()
        receiversLiveData.postValue(handleReceiversResponse(response))
    }

    fun onDataChange(
        date: String,
        poNumber: String,
        category: String,
        product: String,
        make: String,
        receiver: String,
        barcode: String,
        quantity: String
    ){
        saveBtnStatus.postValue(date.isNotEmpty() && poNumber.isNotEmpty()
                && category.isNotEmpty() && product.isNotEmpty() && make.isNotEmpty()
                && receiver.isNotEmpty() && quantity.isNotEmpty()
                && barcode.isNotEmpty() && !barcode.equals("NA", true))
    }

    fun saveAsset(
        date: String,
        poNumber: String,
        make: String,
        barcode: String,
        quantity: String
    ) = viewModelScope.launch {
        createAssetResult.postValue(Resource.Loading())

        val postBody = CreateAssetPostBody(
            deliveryDate = date,
            pONo = poNumber,
            categoryId = selectedCategory?.categoryId!!,
            productId = selectedProduct?.productId!!,
            make = make,
            serialNo = barcode,
            receiverName = selectedReceiver?.userId!!,
            locationId = PreferenceManager.getLong(app, PreferenceManager.Keys.LOCATION_ID, 0),
            modifiedBy = PreferenceManager.getString(app, PreferenceManager.Keys.USER_ID, null),
            qty = quantity.toInt()
        )

        val barcodeDetails = async {
            repo.createAsset(postBody)
        }.await()

        createAssetResult.postValue(handleResponse(barcodeDetails))
    }

    private fun handleResponse(response: Response<CreateAssetResponse>) : Resource<CreateAssetResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.status.equals("SUCCESS", true)) {
                    return Resource.Success(it)
                } else {
                    return Resource.Error("Error saving data!!", null)
                }
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleCategoriesResponse(response: Response<CategoriesResponse>) : Resource<List<Category>> {
        if (response.isSuccessful) {
            response.body()?.let {
                categories.addAll(it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    fun onCategorySelected(category: String, index: Int) = viewModelScope.launch {
        selectedCategory = categories[index]
        productsLiveData.postValue(Resource.Loading())
        val response = repo.getProducts(categories[index].categoryId)
        productsLiveData.postValue(handleProductsResponse(response))
    }

    private fun handleProductsResponse(response: Response<ProductsResponse>) : Resource<List<Product>> {
        if (response.isSuccessful) {
            response.body()?.let {
                products.addAll(it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleReceiversResponse(response: Response<ReceiversResponse>) : Resource<List<Receiver>> {
        if (response.isSuccessful) {
            response.body()?.let {
                receivers.addAll(it)
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    fun onProductSelected(product: String, index: Int) {
        selectedProduct = products[index]
    }

    fun onReceiverSelected(receiver: String, index: Int) {
        selectedReceiver = receivers[index]
    }
}