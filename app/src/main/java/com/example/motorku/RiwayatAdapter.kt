package com.example.motorku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RiwayatAdapter(private val riwayatList: List<ItemRiwayat>) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayat = riwayatList[position]
        holder.namaTextView.text = riwayat.nama
        holder.hargaTextView.text = riwayat.harga
        holder.tanggalTextView.text = riwayat.created_at
//        holder.motorImageView.setImageResource(riwayat.image)

        Picasso.get()
            .load(riwayat.image)  // URL gambar
            .placeholder(R.drawable.ic_loading) // Placeholder saat memuat
            .error(R.drawable.ic_error) // Gambar error jika gagal memuat
            .into(holder.motorImageView) // ImageView yang akan menampilkan gambarimageView) // ImageView yang akan menampilkan gambar
    }

    override fun getItemCount(): Int = riwayatList.size

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.textViewNamaMotor)
        val hargaTextView: TextView = itemView.findViewById(R.id.textViewHarga)
        val tanggalTextView: TextView = itemView.findViewById(R.id.textViewTanggal)
//        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val motorImageView: ImageView = itemView.findViewById(R.id.imageViewMotor)
    }
}
