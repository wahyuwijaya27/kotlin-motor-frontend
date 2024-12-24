package com.example.motorku

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.ResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var cartList: MutableList<ResponseItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        val sharedPreferences = applicationContext.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("ACCESS_TOKEN", "")

        recyclerView = findViewById(R.id.rvCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartList = mutableListOf()

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Unauthorized", Toast.LENGTH_SHORT).show()
        } else {
            val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
            apiInterface.getCartItems("Bearer $token").enqueue(object : Callback<List<ResponseItem>> {
                override fun onResponse(
                    call: Call<List<ResponseItem>>,
                    response: Response<List<ResponseItem>>
                ) {
                    val carts = response.body() ?: emptyList()
                    cartList.clear()
                    for (cart in carts) {
                        cartList.add(
                            ResponseItem(cart.motor, cart.motorId, cart.id)
                        )
                    }

                    adapter = CartAdapter(cartList, token)
                    recyclerView.adapter = adapter
                }

                override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}