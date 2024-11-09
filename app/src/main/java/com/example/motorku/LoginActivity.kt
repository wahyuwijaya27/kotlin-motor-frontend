package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.motorku.api.RetrofitClient
import com.example.motorku.databinding.ActivityLoginBinding
import com.example.motorku.respon.LoginRespon
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var L_txt_username: EditText
    private lateinit var L_txt_password: EditText
    private lateinit var L_btn_2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        L_txt_username = binding.LTxtUsername
        L_txt_password = binding.LTxtPassword
        L_btn_2 = binding.LBtn2

        L_btn_2.setOnClickListener {
            val email = L_txt_username.text.toString()
            val password = L_txt_password.text.toString()

            if (email.isEmpty()) {
                L_txt_username.error = "Username tidak boleh kosong!"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                L_txt_password.error = "Password tidak boleh kosong!"
                return@setOnClickListener
            }

            login(email, password)
        }

        binding.LTxt3.setOnClickListener {
            // Simpan data motor di SharedPreferences sebelum pindah ke RegisterActivity
            val motorDataPref = getSharedPreferences("MotorData", Context.MODE_PRIVATE)
            motorDataPref.edit().apply {
                putString("item_name", intent.getStringExtra("item_name"))
                putString("item_price", intent.getStringExtra("item_price"))
                putString("item_image", intent.getStringExtra("item_image"))
                apply()
            }

            // Pindah ke halaman Register
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login(username: String, password: String) {

        RetrofitClient.api.login(username, password).enqueue(object : Callback<LoginRespon> {
            override fun onResponse(call: Call<LoginRespon>, response: Response<LoginRespon>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    val token = user?.access_token ?: ""

                    // Simpan token di SharedPreferences
                    val sharedPref = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
                    sharedPref.edit().apply {
                        putString("ACCESS_TOKEN", token)
                        apply()
                    }

                    Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, CheckoutActivity::class.java)

                    // Ambil data motor dari Intent dan teruskan ke CheckoutActivity
                    intent.putExtra("item_name", getIntent().getStringExtra("item_name"))
                    intent.putExtra("item_price", getIntent().getStringExtra("item_price"))
                    intent.putExtra("item_image", getIntent().getStringExtra("item_image"))

                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
