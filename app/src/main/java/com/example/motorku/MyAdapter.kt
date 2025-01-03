package com.example.motorku

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(private val context: Context, private val itemList: List<Item>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Memuat gambar motor dari URL menggunakan Picasso
        Picasso.get()
            .load(item.image)  // URL gambar
            .placeholder(R.drawable.ic_loading) // Placeholder saat memuat
            .error(R.drawable.ic_error) // Gambar error jika gagal memuat
            .into(holder.imageView) // ImageView yang akan menampilkan gambar

        Log.d("MyAdapter", "Loading image from URL: ${item.image}")

        // Menampilkan nama motor
        holder.textViewTitle.text = item.name

        // Menampilkan harga motor dan memformatnya
        holder.textViewPrice.text = item.price

        // Mengatur tindakan klik pada card item
        holder.cardView.setOnClickListener {
            val intent = Intent(context, DeskripsiMotorActivity::class.java)
            // Mengirimkan data item ke DeskripsiMotorActivity
            intent.putExtra("motor_id", item.id)  // Menambahkan motor_id
            intent.putExtra("item_name", item.name)
            intent.putExtra("item_price", item.price)
            intent.putExtra("item_image", item.image)
            intent.putExtra("item_specification", item.specification)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val cardView: CardView = itemView.findViewById(R.id.cardItem)
    }
}
