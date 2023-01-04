package com.qikkle.barcodemapping.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qikkle.barcodemapping.R
import com.qikkle.barcodemapping.api.BarcodeMappingRepository
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.model.LoginResponse
import com.qikkle.barcodemapping.utils.PreferenceManager
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(
    application: Application
): AndroidViewModel(application) {

    private val repo by lazy { BarcodeMappingRepository.instance }
    private val loginFormState = MutableLiveData<LoginFormState>()
    private val loginResult: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun getLoginResult(): LiveData<Resource<LoginResponse>> = loginResult;

    fun getLoginFormState(): LiveData<LoginFormState> = loginFormState;

    fun loginDataChanged(username: String?, password: String?) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(LoginFormState(userNameError = R.string.invalid_username))
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(LoginFormState(passwordError = R.string.invalid_password))
        } else {
            loginFormState.setValue(LoginFormState(isDataValid = true))
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        if (isUserNameValid(username) && isPasswordValid(password)) {
            loginResult.postValue(Resource.Loading())
            val loginModelResponse = repo.login(username, password)
            loginResult.postValue(handleResponse(loginModelResponse))
        } else {
            Resource.Error("Enter valid username and password!", null)
        }
    }

    private fun handleResponse(response: Response<LoginResponse>) : Resource<LoginResponse>{
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                if (loginResponse.isNotEmpty()) {
                    PreferenceManager.add(getApplication(), PreferenceManager.Keys.USER_ID, loginResponse[0].userId)
                    return Resource.Success(loginResponse)
                } else {
                    return Resource.Error("Invalid Credentials", null)
                }
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password?.trim()?.isNotEmpty()?:false
    }

    private fun isUserNameValid(userName: String?): Boolean {
        return userName?.trim()?.isNotEmpty()?:false
    }

}