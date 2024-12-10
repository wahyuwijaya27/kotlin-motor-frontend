package com.example.motorku

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorku.R

class PetunjukPembelianActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petunjuk_pembelian)

        val titleTataCara = findViewById<TextView>(R.id.title_tata_cara)
        val contentTataCara = findViewById<View>(R.id.content_tata_cara)

        val titlePerhatian = findViewById<TextView>(R.id.title_perhatian)
        val contentPerhatian = findViewById<View>(R.id.content_perhatian)

        val titleKontak = findViewById<TextView>(R.id.title_kontak)
        val contentKontak = findViewById<View>(R.id.content_kontak)

        val buttonCopyRekening = findViewById<View>(R.id.button_copy_rekening)
        val nomorRekening = findViewById<TextView>(R.id.text_nomor_rekening)

        // Toggle untuk Tata Cara Pembelian
        titleTataCara.setOnClickListener {
            contentTataCara.visibility =
                if (contentTataCara.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        // Toggle untuk Perhatian
        titlePerhatian.setOnClickListener {
            contentPerhatian.visibility =
                if (contentPerhatian.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        // Toggle untuk Kontak
        titleKontak.setOnClickListener {
            contentKontak.visibility =
                if (contentKontak.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        // Salin nomor rekening
        buttonCopyRekening.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Nomor Rekening", nomorRekening.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Nomor rekening disalin", Toast.LENGTH_SHORT).show()
        }
    }
}
