package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeskripsiMotorActivity : AppCompatActivity() {
    private lateinit var btnBeli: Button
    private lateinit var motorImageView: ImageView
    private lateinit var motorNameTextView: TextView
    private lateinit var motorPriceTextView: TextView
    private lateinit var motorSpecTextView: TextView
    private lateinit var back : ImageView
    private lateinit var cartImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_deskripsi_motor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Dm_linear_1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        back = findViewById(R.id.Dm_btn_1)
        back.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }

        // Menginisialisasi komponen UI
        motorImageView = findViewById(R.id.Dm_imgv_1)
        motorNameTextView = findViewById(R.id.Dm_tv_2)
        motorPriceTextView = findViewById(R.id.Dm_tv_3)
        motorSpecTextView = findViewById(R.id.Dm_tv_4)
        btnBeli = findViewById(R.id.btn_beli)
        cartImageView = findViewById(R.id.ic_add_cart)


        // Mendapatkan data dari Intent
        val motorId = intent.getIntExtra("motor_id", -1)  // Pastikan motor_id diteruskan
        val motorName = intent.getStringExtra("item_name")
        val motorPrice = intent.getStringExtra("item_price")
        val motorImageUrl = intent.getStringExtra("item_image")
        val motorSpecification = intent.getStringExtra("item_specification")

        // Menampilkan data ke komponen UI
        motorNameTextView.text = motorName
        motorPriceTextView.text = motorPrice
        motorSpecTextView.text = motorSpecification

        // Memuat gambar menggunakan Picasso
        Picasso.get()
            .load(motorImageUrl)
            .placeholder(R.drawable.ic_loading) // Gambar placeholder
            .error(R.drawable.ic_error) // Gambar jika terjadi error
            .into(motorImageView)

        val sharedPreferences = applicationContext.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("ACCESS_TOKEN", "")

        cartImageView.setOnClickListener {
            if (token.isNullOrEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("motor_id", motorId) // Sertakan motor_id dalam Intent
                intent.putExtra("item_name", motorName)
                intent.putExtra("item_price", motorPrice)
                intent.putExtra("item_image", motorImageUrl)
                intent.putExtra("item_specification", motorSpecification)
                intent.putExtra("type", "keranjang")

                startActivity(intent)
            } else {
                val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
                apiInterface.addToCart("Bearer $token",motorId).enqueue(object : Callback<ApiResponse>{
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        Toast.makeText(applicationContext, "Motor berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.e("AddToCart", "Error: ${t.message}")
                        Toast.makeText(applicationContext, "Motor gagal ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }

        // Tombol "Beli" untuk berpindah ke LoginActivity
        btnBeli.setOnClickListener {
            if (token.isNullOrEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("motor_id", motorId) // Sertakan motor_id dalam Intent
                intent.putExtra("item_name", motorName)
                intent.putExtra("item_price", motorPrice)
                intent.putExtra("item_image", motorImageUrl)

                startActivity(intent)

            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("motor_id", motorId) // Sertakan motor_id dalam Intent
                intent.putExtra("item_name", motorName)
                intent.putExtra("item_price", motorPrice)
                intent.putExtra("item_image", motorImageUrl)

                startActivity(intent)
            }

        }
    }
}
