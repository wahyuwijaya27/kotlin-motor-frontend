package com.example.motorku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient


class CheckoutActivity : AppCompatActivity() {
    private lateinit var btnCheckout: Button
    private lateinit var btnBatal: Button
    private lateinit var motorImageView: ImageView
    private lateinit var motorNameTextView: TextView
    private lateinit var motorPriceTextView: TextView
    private lateinit var namaLengkapEditText: EditText
    private lateinit var alamatLengkapEditText: EditText
    private lateinit var nomorTeleponEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)

        // Inisialisasi komponen UI
        motorImageView = findViewById(R.id.C_img_1)
        motorNameTextView = findViewById(R.id.C_txt_2)
        motorPriceTextView = findViewById(R.id.C_txt_3)
        namaLengkapEditText = findViewById(R.id.C_txt_username)
        alamatLengkapEditText = findViewById(R.id.C_txt_alamat)
        nomorTeleponEditText = findViewById(R.id.C_txt_nomorTelp)

        // Mendapatkan data motor dari Intent
        val motorImageUrl = intent.getStringExtra("item_image")
        val motorName = intent.getStringExtra("item_name")
        val motorPrice = intent.getStringExtra("item_price")
        val motorId = intent.getIntExtra("motor_id", 0)  // Pastikan motor_id dikirim

        motorNameTextView.text = motorName
        motorPriceTextView.text = motorPrice

        Picasso.get()
            .load(motorImageUrl)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_error)
            .into(motorImageView)

        btnCheckout = findViewById(R.id.C_btn_Checkout)
        btnCheckout.setOnClickListener {
            val checkoutData = CheckoutData(
                nama_lengkap = namaLengkapEditText.text.toString(),
                alamat_lengkap = alamatLengkapEditText.text.toString(),
                nomor_telepon = nomorTeleponEditText.text.toString(),
                motor_id = motorId,
                bukti_transaksi = "null" // Sesuaikan atau null jika tidak ada
            )

            val apiService = RetrofitClient.getClient().create(ApiInterface::class.java)
            apiService.checkout(checkoutData).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        // Tampilkan pesan sukses atau lakukan hal lain sesuai kebutuhan
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    // Tangani kegagalan, seperti menampilkan pesan error
                }
            })
        }

        btnBatal = findViewById(R.id.C_btn_batal)
        btnBatal.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
