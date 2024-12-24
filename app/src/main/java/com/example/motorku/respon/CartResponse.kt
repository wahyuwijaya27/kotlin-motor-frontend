package com.example.motorku.respon

import com.google.gson.annotations.SerializedName

data class Motor(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ResponseItem(

	@field:SerializedName("motor")
	val motor: Motor? = null,

	@field:SerializedName("motor_id")
	val motorId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
