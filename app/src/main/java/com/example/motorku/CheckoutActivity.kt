package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.CheckoutResponse
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {
    private lateinit var btnCheckout: Button
    private lateinit var btnBatal: Button
    private lateinit var motorImageView: ImageView
    private lateinit var motorNameTextView: TextView
    private lateinit var motorPriceTextView: TextView
    private lateinit var namaLengkapEditText: EditText
    private lateinit var alamatLengkapEditText: EditText
    private lateinit var nomorTeleponEditText: EditText
    private lateinit var motorIdEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Inisialisasi komponen UI
        motorImageView = findViewById(R.id.C_img_1)
        motorNameTextView = findViewById(R.id.C_txt_2)
        motorPriceTextView = findViewById(R.id.C_txt_3)
        namaLengkapEditText = findViewById(R.id.C_txt_username)
        alamatLengkapEditText = findViewById(R.id.C_txt_alamat)
        nomorTeleponEditText = findViewById(R.id.C_txt_nomorTelp)
        motorIdEditText = findViewById(R.id.C_txt_motorId)

        // Mendapatkan data motor dari Intent
        val motorImageUrl = intent.getStringExtra("item_image")
        val motorName = intent.getStringExtra("item_name")
        val motorPrice = intent.getStringExtra("item_price")

        // Cek apakah motor_id ada di intent atau tidak
        val motorId = intent.getIntExtra("motor_id", -1)
        if (motorId != -1) {
            motorIdEditText.setText(motorId.toString())
        } else {
            Toast.makeText(this, "Motor ID tidak ditemukan di Intent", Toast.LENGTH_SHORT).show()
            motorIdEditText.setText("ID tidak tersedia")
        }

        motorNameTextView.text = motorName
        motorPriceTextView.text = motorPrice

        Picasso.get()
            .load(motorImageUrl)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_error)
            .into(motorImageView)

        // Tombol Checkout
        btnCheckout = findViewById(R.id.C_btn_Checkout)
        btnCheckout.setOnClickListener {
            val namaLengkap = createPartFromString(namaLengkapEditText.text.toString())
            val alamatLengkap = createPartFromString(alamatLengkapEditText.text.toString())
            val nomorTelepon = createPartFromString(nomorTeleponEditText.text.toString())
            val motorIdRequestBody = createPartFromString(motorIdEditText.text.toString())

            val sharedPreferences = applicationContext.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
            val token = "Bearer ${sharedPreferences.getString("ACCESS_TOKEN", "")}"

            val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
            apiInterface.checkout(token, namaLengkap, alamatLengkap, nomorTelepon, motorIdRequestBody)
                .enqueue(object : Callback<CheckoutResponse> {
                    override fun onResponse(call: Call<CheckoutResponse>, response: Response<CheckoutResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CheckoutActivity, "Checkout berhasil", Toast.LENGTH_SHORT).show()

                            // Berpindah ke halaman BuktiPembelianActivity
                            val intent = Intent(this@CheckoutActivity, BuktiPembelianActivity::class.java)
                            intent.putExtra("motor_name", motorName)
                            intent.putExtra("motor_price", motorPrice)
                            intent.putExtra("checkout_id", response.body()?.id)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@CheckoutActivity, "Checkout gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CheckoutResponse>, t: Throwable) {
                        Toast.makeText(this@CheckoutActivity, "Koneksi gagal", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // Tombol Batal
        btnBatal = findViewById(R.id.C_btn_batal)
        btnBatal.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk mengubah teks menjadi RequestBody
    private fun createPartFromString(descriptionString: String): RequestBody {
        return create("text/plain".toMediaTypeOrNull(), descriptionString)
    }
}
