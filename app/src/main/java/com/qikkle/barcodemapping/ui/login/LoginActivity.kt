package com.qikkle.barcodemapping.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.qikkle.barcodemapping.api.Resource
import com.qikkle.barcodemapping.databinding.ActivityLoginBinding
import com.qikkle.barcodemapping.ui.BarcodeMappingActivity
import com.qikkle.barcodemapping.utils.PreferenceManager

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.getLoginFormState().observe(this) { loginFormState ->
            if (loginFormState != null) {
                binding.loginBtn.isEnabled = loginFormState.isDataValid
                loginFormState.userNameError?.let {
                    binding.userNameEt.error = getString(loginFormState.userNameError)
                }

                loginFormState.passwordError?.let {
                    binding.passwordEt.error = getString(loginFormState.passwordError)
                }
            }
        }

        loginViewModel.getLoginResult().observe(this){ loginResult ->
            loginResult?.let {
                when(loginResult) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Snackbar.make(
                            binding.root,
                            loginResult.errorMessage!!,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        startActivity(Intent(this, BarcodeMappingActivity::class.java))
                        finish()
                    }
                }
            }
        }

        binding.userNameEt.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(this.text.toString(),
                    binding.passwordEt.text.toString())
            }
        }

        binding.passwordEt.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(binding.userNameEt.text.toString(),
                    this.text.toString())
            }
        }

        binding.loginBtn.setOnClickListener {
            loginViewModel.login(binding.userNameEt.text.toString(),
                binding.passwordEt.text.toString())
        }

        val userId = PreferenceManager.getString(this, PreferenceManager.Keys.USER_ID, null)
        userId?.let {
            startActivity(Intent(this, BarcodeMappingActivity::class.java))
            finish()
        }
    }


    private fun hideProgressBar() {
        binding.loading.root.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loading.root.visibility = View.VISIBLE
    }

    private fun TextInputEditText.afterTextChanged(afterTextChanged:(String)->Unit){
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                afterTextChanged(p0.toString())
            }
        })
    }
}