package com.example.motorku.api

import com.example.motorku.ApiResponse
import com.example.motorku.CheckoutData
import com.example.motorku.Item
import com.example.motorku.ItemRiwayat
import com.example.motorku.respon.CheckoutResponse
import com.example.motorku.respon.LoginRespon
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiInterface {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginRespon>

    @GET("profile")
    fun profile(
        @Header("Authorization") token: String
    ): Call<LoginRespon>

    @FormUrlEncoded
    @POST("update")
    fun updateUser(
        @Header("Authorization") token: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @GET("motor/recommended")
    fun getRecommendedMotors(
        @Header("Authorization") token: String
    ): Call<List<Item>>

    @Multipart
    @POST("checkout")
    fun checkout(
        @Header("Authorization") token: String,
        @Part("nama_lengkap") namaLengkap: RequestBody,
        @Part("alamat_lengkap") alamatLengkap: RequestBody,
        @Part("nomor_telepon") nomorTelepon: RequestBody,
        @Part("motor_id") motorId: RequestBody,
    ): Call<CheckoutResponse>

    @GET("checkouts")
    fun getCheckouts(
        @Header("Authorization") token: String
    ): Call<List<ItemRiwayat>>

    @Multipart
    @POST("checkout/upload-bukti/{id}")
    fun uploadBuktiTransaksi(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part bukti_transaksi: MultipartBody.Part
    ): Call<ApiResponse>



}
