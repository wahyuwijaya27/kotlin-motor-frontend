package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.ResetRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonResetPassword: Button
    private var phoneNumber: String? = null
    private var otp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        editTextNewPassword = findViewById(R.id.etNewPassword)
        editTextConfirmPassword = findViewById(R.id.etConfirmPassword)
        buttonResetPassword = findViewById(R.id.btnResetPassword)

        phoneNumber = intent.getStringExtra("phoneNumber")
        otp = intent.getStringExtra("otp")

        buttonResetPassword.setOnClickListener {
            val newPassword = editTextNewPassword.text.toString()
            val c_password = editTextConfirmPassword.text.toString()

            resetPassword(phoneNumber,otp,newPassword, c_password)
        }
    }

    private fun resetPassword(phoneNumber: String?, otp: String?, newPassword: String, cPassword: String) {
        if (newPassword.isNotEmpty() && cPassword.isNotEmpty() && newPassword == cPassword) {
            val resetRequest = ResetRequest(phoneNumber!!, otp!!, newPassword,cPassword)
            RetrofitClient.api.resetPassword(resetRequest)
                .enqueue(object :Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Toast.makeText(this@ResetPasswordActivity, "Reset Password Berhasil", Toast.LENGTH_SHORT).show()
                        val motorDataPref = getSharedPreferences("MotorData", Context.MODE_PRIVATE)
                        val motorId = motorDataPref.getInt("motor_id", -1)
                        val motorName = motorDataPref.getString("item_name", "")
                        val motorPrice = motorDataPref.getString("item_price", "")
                        val motorImageUrl = motorDataPref.getString("item_image", "")

                        // Siapkan intent untuk kembali ke LoginActivity dengan data motor
                        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java).apply {
                            putExtra("motor_id", motorId)
                            putExtra("item_name", motorName)
                            putExtra("item_price", motorPrice)
                            putExtra("item_image", motorImageUrl)
                        }
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@ResetPasswordActivity, "Reset Password Gagal", Toast.LENGTH_SHORT).show()
                    }

                })
        } else {
            Toast.makeText(this, "Password tidak sesuai atau kosong!", Toast.LENGTH_SHORT).show()
        }

    }
}
