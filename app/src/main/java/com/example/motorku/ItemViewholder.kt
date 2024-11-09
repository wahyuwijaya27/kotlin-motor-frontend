package com.example.motorku

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val barangImageView: ImageView = itemView.findViewById(R.id.imageView)
    val namaTextView: TextView = itemView.findViewById(R.id.textViewTitle)
    val jumlahTextView: TextView = itemView.findViewById(R.id.textViewPrice)
}