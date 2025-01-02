package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import com.example.motorku.databinding.ActivityLoginBinding
import com.example.motorku.respon.LoginRespon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var L_txt_username: EditText
    private lateinit var L_txt_password: EditText
    private lateinit var L_btn_2: Button
    private lateinit var L_txt_forgotPassword: TextView

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
        L_txt_forgotPassword = binding.LTxtForgotPassword

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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        L_txt_forgotPassword.setOnClickListener {
            val intent = Intent(this, InputPhoneActivity::class.java)
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

                    val intent = if (getIntent().getStringExtra("type") == "keranjang") {
                        Intent(this@LoginActivity, DeskripsiMotorActivity::class.java).apply {
                            putExtra("motor_id", getIntent().getIntExtra("motor_id", -1))
                            putExtra("item_name", getIntent().getStringExtra("item_name"))
                            putExtra("item_price", getIntent().getStringExtra("item_price"))
                            putExtra("item_image", getIntent().getStringExtra("item_image"))
                            putExtra("item_specification", getIntent().getStringExtra("item_specification"))
                        }
                    } else {
                        Intent(this@LoginActivity, CheckoutActivity::class.java).apply {
                            putExtra("motor_id", getIntent().getIntExtra("motor_id", -1))
                            putExtra("item_name", getIntent().getStringExtra("item_name"))
                            putExtra("item_price", getIntent().getStringExtra("item_price"))
                            putExtra("item_image", getIntent().getStringExtra("item_image"))
                        }
                    }
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
