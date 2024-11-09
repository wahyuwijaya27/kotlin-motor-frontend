package com.example.motorku.api

import com.example.motorku.ApiResponse
import com.example.motorku.CheckoutData
import com.example.motorku.Item
import com.example.motorku.respon.LoginRespon
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


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

    @POST("checkout")
    fun checkout(@Body checkoutData: CheckoutData): Call<ApiResponse>
}
