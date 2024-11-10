package com.example.motorku

import okhttp3.MultipartBody

data class CheckoutData(
    val nama_lengkap: String,
    val alamat_lengkap: String,
    val nomor_telepon: String,
    val motor_id: Int,
    val bukti_transaksi: MultipartBody.Part
)
