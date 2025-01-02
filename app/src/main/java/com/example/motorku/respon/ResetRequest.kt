package com.example.motorku.respon

data class ResetRequest(
    val phone : String,
    val otp : String,
    val password : String,
    val c_password : String
)
