package com.example.motorku.api

import com.example.motorku.ApiResponse
import com.example.motorku.Item
import com.example.motorku.ItemRiwayat
import com.example.motorku.respon.CheckoutResponse
import com.example.motorku.respon.LoginRespon
import com.example.motorku.respon.PhoneRequest
import com.example.motorku.respon.ResetRequest
import com.example.motorku.respon.ResponseItem
import com.example.motorku.respon.VerifyRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        @Field("phone") phone: String,
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

    @FormUrlEncoded
    @POST("carts")
    fun addToCart(
        @Header("Authorization") token: String,
        @Field("motor_id") motorId: Int,
    ): Call<ApiResponse>

    @GET("carts")
    fun getCartItems(
        @Header("Authorization") token: String
    ): Call<List<ResponseItem>>

    @DELETE("carts/{id}")
    fun deleteCartItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ApiResponse>


    @POST("forgot-password/send-otp")
    fun sendOtp(@Body request: PhoneRequest): Call<ResponseBody>

    @POST("forgot-password/verify-otp")
    fun verifyOtp(@Body request: VerifyRequest): Call<ResponseBody>

    @POST("forgot-password/reset-password")
    fun resetPassword(@Body request: ResetRequest): Call<ResponseBody>


}
