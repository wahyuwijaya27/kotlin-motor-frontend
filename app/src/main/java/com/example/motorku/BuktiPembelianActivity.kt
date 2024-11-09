package com.example.motorku

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import com.example.motorku.R

class BuktiPembelianActivity : AppCompatActivity() {

    private lateinit var back : ImageView
    private lateinit var imageView: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnCaptureImage: Button
    private lateinit var btnSubmit: Button

    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti_pembelian)

        imageView = findViewById(R.id.Bp_imageView)
        btnSelectImage = findViewById(R.id.Bp_btn_pilihGambar)
        btnCaptureImage = findViewById(R.id.btn_ambilGambar)
        btnSubmit = findViewById(R.id.btnSubmit)

        checkAndRequestPermissions()

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnCaptureImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
        }

        btnSubmit.setOnClickListener {
            showSuccessDialog()
        }

        back = findViewById(R.id.Bp_btn_back)
        back.setOnClickListener {
            val i = Intent(this, CheckoutActivity::class.java)
            startActivity(i)
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImage = data?.data
                    imageView.setImageURI(selectedImage)
                }

                CAPTURE_IMAGE_REQUEST -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(photo)
                }
            }
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pembayaran Berhasil")
        builder.setMessage("Bukti pembayaran berhasil dikirim. Terima kasih!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Kembali ke aktivitas sebelumnya atau tutup aplikasi
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
