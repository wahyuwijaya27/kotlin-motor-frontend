package com.example.motorku.respon

data class CheckoutResponse(
    val id: Int,
    val nama_lengkap: String,
    val alamat_lengkap: String,
    val nomor_telepon: String,
    val motor_id: Int,
    val users_id: Int,
    val created_at: String,
    val updated_at: String
)

