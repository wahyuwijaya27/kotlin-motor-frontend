package com.example.motorku

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti_pembelian)

        btnSelectImage = findViewById(R.id.Bp_btn_pilihGambar)
        btnSubmit = findViewById(R.id.btnSubmit)
        imageView = findViewById(R.id.Bp_imageView)

        // Tombol Upload Bukti
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
            val call = apiInterface.uploadBuktiTransaksi(token, checkout_id,buktiTransaksiPart)

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@BuktiPembelianActivity, "Bukti transaksi berhasil diunggah", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke halaman sebelumnya
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

    // Membuka galeri untuk memilih gambar
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Fungsi untuk mengubah teks menjadi RequestBody
    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), descriptionString)
    }

    // Fungsi untuk mengonversi URI ke Multipart
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


    // Fungsi untuk mendapatkan path asli dari URI
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val path = cursor?.getString(idx ?: -1)
        cursor?.close()
        return path ?: throw IllegalArgumentException("Gagal mendapatkan path dari URI")
    }



    // Menangani hasil dari pemilihan gambar dari galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            try {
                // Mendapatkan Bitmap dari URI yang dipilih
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageView.setImageBitmap(bitmap) // Menampilkan gambar di ImageView
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

