package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorku.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var itemList: MutableList<Item>
    private lateinit var filteredItemList: MutableList<Item>
    private lateinit var searchView: SearchView
    private lateinit var icCart: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        icCart = view.findViewById(R.id.ic_cart)

        icCart.setOnClickListener {
            val intent = Intent(requireContext(), CartActivity::class.java)
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        itemList = mutableListOf()
        filteredItemList = mutableListOf()
        myAdapter = MyAdapter(requireContext(), filteredItemList)
        recyclerView.adapter = myAdapter

        val categoryall = view.findViewById<ImageView>(R.id.categoryall_image)
        val category1 = view.findViewById<ImageView>(R.id.category1_image)
        val category2 = view.findViewById<ImageView>(R.id.category2_image)
        val category3 = view.findViewById<ImageView>(R.id.category3_image)
        val category4 = view.findViewById<ImageView>(R.id.category4_image)
        val category5 = view.findViewById<ImageView>(R.id.category5_image)
        val category6 = view.findViewById<ImageView>(R.id.category6_image)

        categoryall.setOnClickListener { showAllItems() }
        category1.setOnClickListener { filterItems("Matic") }
        category2.setOnClickListener { filterItems("Bebek") }
        category3.setOnClickListener { filterItems("Sport") }
        category4.setOnClickListener { filterItems("Trail") }
        category5.setOnClickListener { filterItems("Classic") }
        category6.setOnClickListener { filterItems("Moge") }

        searchView = view.findViewById(R.id.H_search)
        searchView.setOnQueryTextListener(this)

        // Memanggil API untuk mendapatkan rekomendasi motor
        fetchRecommendedMotors()
        return view
    }

    private fun fetchRecommendedMotors() {
        // Mendapatkan token dari SharedPreferences (misalnya)
        val sharedPreferences = requireContext().getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        val token = "Bearer ${sharedPreferences.getString("ACCESS_TOKEN", "")}"

        RetrofitClient.api.getRecommendedMotors(token).enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful && response.body() != null) {
                    itemList.clear()
                    itemList.addAll(response.body()!!)
                    filteredItemList.clear()
                    filteredItemList.addAll(itemList)
                    myAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Toast.makeText(context, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAllItems() {
        filteredItemList.clear()
        filteredItemList.addAll(itemList)
        myAdapter.notifyDataSetChanged()
    }

    private fun filterItems(category: String) {
        filteredItemList.clear()
        filteredItemList.addAll(itemList.filter { it.brand.equals(category, ignoreCase = true) })
        myAdapter.notifyDataSetChanged()
    }

    private fun filterItemsByQuery(query: String) {
        filteredItemList.clear()
        filteredItemList.addAll(itemList.filter { it.name.contains(query, ignoreCase = true) })
        myAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            filterItemsByQuery(query)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            filterItemsByQuery(newText)
        }
        return false
    }
}
