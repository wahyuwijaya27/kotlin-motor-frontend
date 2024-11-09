package com.example.motorku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.motorku.api.RetrofitClient
import com.example.motorku.respon.LoginRespon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfilFragment : Fragment() {
    private lateinit var btnEditProfil: Button
    private lateinit var btnLogout: Button
    private lateinit var Pr_txt_username: TextView
    private lateinit var logout_button: Button

    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        Pr_txt_username = view.findViewById(R.id.Pr_txt_username)
        logout_button = view.findViewById(R.id.logout_button)

        logout_button.setOnClickListener {
            logout()
        }

        loadUserData()
        return view
    }



    private fun loadUserData() {
        val sharedPref = activity?.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

        val call = RetrofitClient.api.profile("Bearer $token")

        call.enqueue(object : Callback<LoginRespon> {
            override fun onResponse(
                call: Call<LoginRespon>,
                response: Response<LoginRespon>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("ProfilFragment", "User data: $user")
                    Pr_txt_username.text = user?.username
                } else {
                    // Handle if failed to fetch user data
                    Log.e("ProfilFragment", "Failed to load user data: ${response.message()}")
                    // Display error message to the user
                    Toast.makeText(activity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                // Handle if there is a network error
                Log.e("ProfilFragment", "Error: ${t.message}")
                // Display error message to the user
                Toast.makeText(activity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnEditProfil = view.findViewById(R.id.Pr_btn_editprofil)
        btnEditProfil.setOnClickListener {
            Log.d("ProfilFragment", "Edit Profile button clicked")
            val intent = Intent(activity, EditProfilActivity::class.java)
            startActivity(intent)
        }

        btnLogout = view.findViewById(R.id.logout_button)
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Apakah Anda yakin ingin logout?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            // Implementasi proses logout di sini
            logout()
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun logout() {

        // Hapus token dari SharedPreferences
        val sharedPref = activity?.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove("ACCESS_TOKEN")
            apply()

            // Setelah logout, navigasi ke Activity login atau tutup aplikasi
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }
}
