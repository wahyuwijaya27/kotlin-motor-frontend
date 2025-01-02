package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.api.RetrofitClient
import com.example.motorku.databinding.ActivityRegisterBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RBtnRegister.setOnClickListener {
            val username = binding.RTxtUsername.text.toString()
            val email = binding.RTxtEmail.text.toString()
            val phone = binding.RTxtPhone.text.toString() // Mengambil input nomor telepon
            val password1 = binding.RTxtPassword.text.toString()
            val password2 = binding.RTxtRepassword.text.toString()

            // Validasi input
            if (username.isEmpty()) {
                binding.RTxtUsername.error = "Username tidak boleh kosong!"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.RTxtEmail.error = "Email tidak boleh kosong!"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.RTxtEmail.error = "Email tidak valid!"
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                binding.RTxtPhone.error = "Nomor telepon tidak boleh kosong!"
                return@setOnClickListener
            }

            if (phone.length < 10 || phone.length > 13) {
                binding.RTxtPhone.error = "Nomor telepon harus antara 10-13 digit!"
                return@setOnClickListener
            }

            if (password1.isEmpty()) {
                binding.RTxtPassword.error = "Password tidak boleh kosong!"
                return@setOnClickListener
            }

            if (password1.length < 6) {
                binding.RTxtPassword.error = "Password harus minimal 6 karakter!"
                return@setOnClickListener
            }

            if (password1 != password2) {
                binding.RTxtRepassword.error = "Password tidak cocok!"
                return@setOnClickListener
            }

            // Panggil fungsi register jika validasi berhasil
            register(username, email, phone, password1, password2)
        }
    }

    private fun register(username: String, email: String, phone: String, password: String, passwordConfirmation: String) {
        RetrofitClient.api.register(username, email, phone, password, passwordConfirmation)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Register berhasil, silahkan login.", Toast.LENGTH_LONG).show()

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

                        // Pindah ke LoginActivity
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register gagal: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
