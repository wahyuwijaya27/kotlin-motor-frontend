package com.example.motorku

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.motorku.api.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfilActivity : AppCompatActivity() {

    private lateinit var btnEditFoto: Button
    private lateinit var imageView: ImageView
    private lateinit var Ep_et_username: EditText
    private lateinit var Ep_et_password: EditText
    private lateinit var Ep_btn_simpan: Button

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.Ep_foto_profil)
        btnEditFoto = findViewById(R.id.Ep_btn_editFoto)
        Ep_et_username = findViewById(R.id.Ep_et_username)
        Ep_et_password = findViewById(R.id.Ep_et_password)
        Ep_btn_simpan = findViewById(R.id.Ep_btn_simpan)

        btnEditFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        Ep_btn_simpan.setOnClickListener {
            val username = Ep_et_username.text.toString()
            val password = Ep_et_password.text.toString()

            // Cek apakah token tidak null atau kosong
            val sharedPreferences = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("ACCESS_TOKEN", "")

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "Token tidak ditemukan. Harap login kembali.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.api.updateUser("Bearer $token", username, password).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfilActivity, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditProfilActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditProfilActivity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@EditProfilActivity, "Terjadi kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Meminta izin jika diperlukan
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val permissions = listOf(
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
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                val circularBitmap = getCircularBitmap(bitmap)
                imageView.setImageBitmap(circularBitmap)
            }
        }
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val diameter = Math.min(width, height)

        val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true

        val path = Path()
        path.addCircle(
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            Path.Direction.CCW
        )

        canvas.clipPath(path)
        canvas.drawBitmap(
            bitmap,
            ((diameter - width) / 2).toFloat(),
            ((diameter - height) / 2).toFloat(),
            paint
        )

        return output
    }
}
