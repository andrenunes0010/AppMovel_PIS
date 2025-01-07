package com.example.appmovel_pis.ui.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.MenuFragment
import com.example.appmovel_pis.ui.fragments.ProfileFragment
import com.example.appmovel_pis.ui.fragments.SensorFragment
import com.example.appmovel_pis.ui.fragments.SystemFragment
import com.example.appmovel_pis.ui.main.MainActivity

class MenuPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        // Get a reference to the ImageView
        val iconMenuImageView = findViewById<ImageView>(R.id.iconMenubtn)
        val iconLogOutImageView = findViewById<ImageView>(R.id.iconLogOutbtn)
        val iconProfileImageView = findViewById<ImageView>(R.id.iconProfilebtn)
        val iconSystemImageView = findViewById<ImageView>(R.id.iconSystembtn)

        // Set a click listener
        iconMenuImageView.setOnClickListener {
            // Load the MenuFragment
            val menuFragment = MenuFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, menuFragment) // Replace the container with MenuFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        iconLogOutImageView.setOnClickListener {
            // Log Out
            val intent = Intent(this@MenuPage, MainActivity::class.java)
            startActivity(intent)
        }

        iconProfileImageView.setOnClickListener {
            // Load the ProfileFragment
            val profileFragment = ProfileFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, profileFragment) // Replace the container with MenuFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        iconSystemImageView.setOnClickListener {
            // Load the SystemFragment
            val systemFragment = SystemFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, systemFragment) // Replace the container with MenuFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}