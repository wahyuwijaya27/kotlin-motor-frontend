package com.example.motorku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
//    private lateinit var backHome : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeFragment=HomeFragment()
        val riwayatFragment=RiwayatFragment()
        val aboutFragment=AboutFragment()
        val profilFragment=ProfilFragment()
//        val profileFragment=ProfileFragment()

        setCurrentFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->setCurrentFragment(homeFragment)
                R.id.nav_riwayat->setCurrentFragment(riwayatFragment)
                R.id.nav_about->setCurrentFragment(aboutFragment)
                R.id.nav_profil->setCurrentFragment(profilFragment)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.F_layout_1,fragment)
            commit()
        }
//        backHome = findViewById(R.id.H_btn_1)
//        backHome.setOnClickListener {
//            val i = Intent(this, LoginActivity::class.java)
//            startActivity(i)
//        }
    }

