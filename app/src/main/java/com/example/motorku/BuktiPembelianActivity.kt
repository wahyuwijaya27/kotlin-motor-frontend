package com.example.motorku

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BuktiPembelianActivity : AppCompatActivity() {

    private lateinit var btnSelectImage: Button
    private lateinit var btnSubmit: Button
    private lateinit var imageView: ImageView
    private lateinit var tvCountdown: TextView

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti_pembelian)

        btnSelectImage = findViewById(R.id.Bp_btn_pilihGambar)
        btnSubmit = findViewById(R.id.btnSubmit)
        imageView = findViewById(R.id.Bp_imageView)
        tvCountdown = findViewById(R.id.tvCountdown)

        startCountdown()

        btnSelectImage.setOnClickListener {
            openGallery()
        }

        btnSubmit.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(this, "Harap pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val buktiTransaksiPart = imageUri?.let { prepareFilePart("bukti_transaksi", it) }
            if (buktiTransaksiPart == null) {
                Toast.makeText(this, "Gagal menyiapkan file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = applicationContext.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
            val token = "Bearer ${sharedPreferences.getString("ACCESS_TOKEN", "")}"
            val checkout_id = intent.getIntExtra("checkout_id", 0)

            val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
            val call = apiInterface.uploadBuktiTransaksi(token, checkout_id, buktiTransaksiPart)

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@BuktiPembelianActivity, "Bukti transaksi berhasil diunggah", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@BuktiPembelianActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@BuktiPembelianActivity, "Gagal mengunggah bukti transaksi", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@BuktiPembelianActivity, "Terjadi kesalahan koneksi", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun startCountdown() {
        val totalTime = 5 * 60 * 1000L // 5 minutes in milliseconds

        countdownTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                tvCountdown.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                tvCountdown.text = "Waktu habis!"
                btnSubmit.isEnabled = false // Disable submit button when time is up
                btnSubmit.setBackgroundColor(ContextCompat.getColor(this@BuktiPembelianActivity, R.color.gray)) // Change button color to gray
            }

        }.start()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(fileUri) ?: throw IOException("Gagal membaca URI")
        val file = File(cacheDir, "upload_image.jpg")
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
