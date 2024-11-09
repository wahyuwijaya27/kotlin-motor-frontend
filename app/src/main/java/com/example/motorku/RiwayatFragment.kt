package com.example.motorku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RiwayatFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MotorAdapter
    private lateinit var riwayatList: MutableList<ItemRiwayat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewRiwayat)
        recyclerView.layoutManager = LinearLayoutManager(context)

        riwayatList = mutableListOf()
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))
        riwayatList.add(ItemRiwayat("Kawasaki Ninja", "Rp. 50.000.000,00", "13-05-2024", "LUNAS", R.drawable.motor1))

        // Pass context and riwayatList to the MotorAdapter constructor
        myAdapter = MotorAdapter(riwayatList)
        recyclerView.adapter = myAdapter

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RiwayatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
