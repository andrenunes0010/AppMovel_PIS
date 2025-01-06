package com.example.appmovel_pis.ui.menu

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.MenuFragment

class MenuPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        // Get a reference to the ImageView
        val iconMenuImageView = findViewById<ImageView>(R.id.iconMenubtn)

        // Set a click listener
        iconMenuImageView.setOnClickListener {
            // Load the MenuFragment
            val menuFragment = MenuFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, menuFragment) // Replace the container with MenuFragment
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