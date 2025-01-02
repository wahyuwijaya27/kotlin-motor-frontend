package com.example.motorku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.PhoneRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputPhoneActivity : AppCompatActivity() {

    private lateinit var editTextPhoneNumber: EditText
    private lateinit var buttonSendOtp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_phone)

        editTextPhoneNumber = findViewById(R.id.etPhoneNumber)
        buttonSendOtp = findViewById(R.id.btnSendOtp)

        buttonSendOtp.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString()

            validasiPhone(phoneNumber)
        }
    }

    private fun validasiPhone(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            val phoneRequest = PhoneRequest(phoneNumber);

            RetrofitClient.api.sendOtp(phoneRequest)
                .enqueue(object :Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@InputPhoneActivity, "OTP terkirim!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@InputPhoneActivity, InputOtpActivity::class.java)
                            intent.putExtra("phoneNumber", phoneNumber)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@InputPhoneActivity, "Gagal mengirim OTP!", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "Masukkan nomor telepon!", Toast.LENGTH_SHORT).show()
        }

    }
}
