package com.qikkle.barcodemapping.ui.login

data class LoginFormState(
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
