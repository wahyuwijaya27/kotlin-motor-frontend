package com.example.motorku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.VerifyRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputOtpActivity : AppCompatActivity() {

    private lateinit var editTextOtp: EditText
    private lateinit var buttonVerifyOtp: Button
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_otp)

        editTextOtp = findViewById(R.id.etOtp)
        buttonVerifyOtp = findViewById(R.id.btnVerifyOtp)

        phoneNumber = intent.getStringExtra("phoneNumber")

        buttonVerifyOtp.setOnClickListener {
            val otp = editTextOtp.text.toString()
            validasiOtp(otp, phoneNumber)
        }
    }

    private fun validasiOtp(otp: String, phoneNumber: String?) {
        if (!phoneNumber.isNullOrEmpty() && otp.isNotEmpty()) {
            val verifyRequest = VerifyRequest(phoneNumber,otp)

            RetrofitClient.api.verifyOtp(verifyRequest)
                .enqueue(object :Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Toast.makeText(this@InputOtpActivity, "OTP Valid", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@InputOtpActivity, ResetPasswordActivity::class.java)
                        intent.putExtra("phoneNumber", phoneNumber)
                        intent.putExtra("otp", otp)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@InputOtpActivity, "OTP Tidak Valid", Toast.LENGTH_SHORT).show()
                    }

                })
        } else {
            Toast.makeText(this, "Tidak Valid", Toast.LENGTH_SHORT).show()
        }
    }
}
