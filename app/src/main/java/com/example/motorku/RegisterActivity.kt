package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.motorku.api.RetrofitClient
import com.example.motorku.databinding.ActivityRegisterBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var back : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        back = findViewById(R.id.R_btn_1)
//        back.setOnClickListener {
//            val i = Intent(this, LoginActivity::class.java)
//            startActivity(i)
//        }

        binding.RBtnRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        binding.RBtnRegister.setOnClickListener {
            val username = binding.RTxtUsername.text.toString()
            val email = binding.RTxtEmail.text.toString()
            val password1 = binding.RTxtPassword.text.toString()
            val password2 = binding.RTxtRepassword.text.toString()

            if (username.isEmpty()) {
                binding.RTxtUsername.error = "Username tidak boleh kosong!"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.RTxtEmail.error = "Email tidak sesuai format!"
                return@setOnClickListener
            }

            if (password1.isEmpty() || password2.isEmpty() || password1 != password2) {
                Toast.makeText(this, "Password tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            register(username, email, password1, password2)
        }
    }

    private fun register(username: String, email: String, password: String, passwordConfirmation: String) {
        RetrofitClient.api.register(username, email, password, passwordConfirmation).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("RegisterActivity", "onResponse called")
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Register berhasil, silahkan login.", Toast.LENGTH_LONG).show()
                    }

                    // Ambil data dari SharedPreferences
                    val motorDataPref = getSharedPreferences("MotorData", Context.MODE_PRIVATE)
                    val motorId = motorDataPref.getInt("motor_id", -1)
                    val motorName = motorDataPref.getString("item_name", "")
                    val motorPrice = motorDataPref.getString("item_price", "")
                    val motorImageUrl = motorDataPref.getString("item_image", "")

                    // Siapkan intent untuk kembali ke LoginActivity dengan data motor
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                        putExtra("motor_id", motorId)
                        putExtra("item_name", motorName)
                        putExtra("item_price", motorPrice)
                        putExtra("item_image", motorImageUrl)
                    }

                    startActivity(intent)
                    finish()
                } else {
                    Log.d("RegisterActivity", "Response not successful")
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Register gagal: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("RegisterActivity", "onFailure called")
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}