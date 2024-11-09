package com.example.motorku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MotorAdapter(private val motorList: List<ItemRiwayat>) : RecyclerView.Adapter<MotorAdapter.MotorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return MotorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MotorViewHolder, position: Int) {
        val motor = motorList[position]
        holder.namaTextView.text = motor.nama
        holder.hargaTextView.text = motor.harga
        holder.tanggalTextView.text = motor.tanggal
        holder.statusTextView.text = motor.status
        holder.motorImageView.setImageResource(motor.imageResource)
    }

    override fun getItemCount(): Int = motorList.size

    class MotorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.textViewNamaMotor)
        val hargaTextView: TextView = itemView.findViewById(R.id.textViewHarga)
        val tanggalTextView: TextView = itemView.findViewById(R.id.textViewTanggal)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val motorImageView: ImageView = itemView.findViewById(R.id.imageViewMotor)
    }
}
