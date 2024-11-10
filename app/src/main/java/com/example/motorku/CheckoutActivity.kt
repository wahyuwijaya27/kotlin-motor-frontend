package com.example.motorku

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

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
    private lateinit var btnUploadBukti: Button
    private lateinit var imgBuktiTransaksi: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

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
        btnUploadBukti = findViewById(R.id.C_btn_uploadBukti)
        imgBuktiTransaksi = findViewById(R.id.C_img_buktiTransaksi)

        // Mendapatkan data motor dari Intent
        val motorImageUrl = intent.getStringExtra("item_image")
        val motorName = intent.getStringExtra("item_name")
        val motorPrice = intent.getStringExtra("item_price")

        // Cek apakah motor_id ada di intent atau tidak
        val motorId = intent.getIntExtra("motor_id", -1)
        if (motorId != -1) {
            // motor_id berhasil dibawa, tampilkan datanya
            motorIdEditText.setText(motorId.toString())
        } else {
            // motor_id tidak ditemukan di Intent, tampilkan pesan
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
            val motorId = createPartFromString(motorIdEditText.text.toString())

            val buktiTransaksiPart = imageUri?.let { prepareFilePart("bukti_transaksi", it) }

            val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
            apiInterface.checkout(namaLengkap, alamatLengkap, nomorTelepon, motorId, buktiTransaksiPart)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CheckoutActivity, "Checkout berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CheckoutActivity, "Checkout gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
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

        // Tombol Upload Bukti
        btnUploadBukti.setOnClickListener {
            openGallery()
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
        val file = File(getRealPathFromURI(fileUri))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    // Fungsi untuk mendapatkan path asli dari URI
    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        return cursor?.getString(columnIndex ?: -1).also {
            cursor?.close()
        }
    }


    // Menangani hasil dari pemilihan gambar dari galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            try {
                // Mendapatkan Bitmap dari URI yang dipilih
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imgBuktiTransaksi.setImageBitmap(bitmap) // Menampilkan gambar di ImageView
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
