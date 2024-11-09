package com.example.motorku.respon

data class LoginRespon(
    val access_token: String,
    val token_type: String,
    val username: String,
    val password : String,
)
