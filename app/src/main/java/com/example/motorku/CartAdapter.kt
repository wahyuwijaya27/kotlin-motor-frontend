package com.example.motorku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.ResponseItem
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter (private val cartList: MutableList<ResponseItem>, val token: String) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgMotor)
        val textViewName: TextView = itemView.findViewById(R.id.tvMotorName)
        val textViewPrice: TextView = itemView.findViewById(R.id.tvMotorPrice)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartList[position]
        Picasso.get()
            .load(cart.motor!!.image)  // URL gambar
            .placeholder(R.drawable.ic_loading) // Placeholder saat memuat
            .error(R.drawable.ic_error) // Gambar error jika gagal memuat
            .into(holder.imageView)

        holder.textViewName.text = cart.motor!!.name
        holder.textViewPrice.text = cart.motor.price
        holder.btnDelete.setOnClickListener {
            val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
            apiInterface.deleteCartItem("Bearer $token", cart.id!!).enqueue(object :
                Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    cartList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(holder.itemView.context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    override fun getItemCount(): Int = cartList.size


}