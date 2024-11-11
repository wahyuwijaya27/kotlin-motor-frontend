package com.example.motorku

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorku.api.ApiInterface
import com.example.motorku.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var riwayatAdapter: RiwayatAdapter
    private lateinit var riwayatList: MutableList<ItemRiwayat>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewRiwayat)
        recyclerView.layoutManager = LinearLayoutManager(context)

        riwayatList = mutableListOf()
        loadRiwayatData() // Panggil fungsi untuk memuat data checkout

        riwayatAdapter = RiwayatAdapter(riwayatList)
        recyclerView.adapter = riwayatAdapter

        return view
    }

    private fun loadRiwayatData() {
        val apiInterface = RetrofitClient.instance.create(ApiInterface::class.java)

        val sharedPreferences = requireContext().getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        val token = "Bearer ${sharedPreferences.getString("ACCESS_TOKEN", "")}"
        // Mengambil daftar checkout
        val call = apiInterface.getCheckouts(token)

        call.enqueue(object : Callback<List<ItemRiwayat>> {
            override fun onResponse(call: Call<List<ItemRiwayat>>, response: Response<List<ItemRiwayat>>) {
                if (response.isSuccessful) {
                    val checkouts = response.body() ?: emptyList()
                    riwayatList.clear()
                    for (checkout in checkouts) {
                        riwayatList.add(
                            ItemRiwayat(
                                checkout.nama,
                                "Rp. ${checkout.harga}",
                                checkout.created_at,
                                checkout.image
                            )
                        )
                    }
                    riwayatAdapter.notifyDataSetChanged() // Update RecyclerView
                } else {
                    // Tangani jika response tidak sukses
                    Toast.makeText(context, "Gagal memuat riwayat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemRiwayat>>, t: Throwable) {
                // Tangani kegagalan
                Toast.makeText(context, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
