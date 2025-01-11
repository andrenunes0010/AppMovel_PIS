package com.example.appmovel_pis.ui.menu

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.InformationFragment
import com.example.appmovel_pis.ui.fragments.InstallFragment
import com.example.appmovel_pis.ui.fragments.ProfileFragment
import com.example.appmovel_pis.ui.fragments.SensorFragment
import com.example.appmovel_pis.ui.interfaces.IMenu

class MenuPage : AppCompatActivity(), IMenu {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        // Get a reference to the ImageView
        val iconSensorImageView = findViewById<ImageView>(R.id.iconSensorbtn)
        val iconInstallImageView = findViewById<ImageView>(R.id.iconMenubtn)
        val iconProfileImageView = findViewById<ImageView>(R.id.iconProfilebtn)
        val iconInformationImageView = findViewById<ImageView>(R.id.iconSystembtn)

        // Check if the fragment is already added (e.g., after a configuration change)
        if (savedInstanceState == null) {
            val sensorFragment = SensorFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, sensorFragment) // Use replace or add
                .commit()
        }

        iconSensorImageView.setOnClickListener {
            // Load the SensorFragment
            val sensorFragment = SensorFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, sensorFragment) // Replace the container with SensorFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        iconInstallImageView.setOnClickListener {
            // Load the InstallFragment
            val installFragment = InstallFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, installFragment) // Replace the container with MenuFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        iconProfileImageView.setOnClickListener {
            // Load the ProfileFragment
            val profileFragment = ProfileFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, profileFragment) // Replace the container with MenuFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        iconInformationImageView.setOnClickListener {
            // Load the InfoFragment
            val informationFragment = InformationFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, informationFragment) // Replace the container with MenuFragment
                .addToBackStack(null) // Add to back stack for navigation
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun opcao1() {
        // trocar o fragmebnto pretendido
    }

    override fun opcao2() {
        // trocar o fragmebnto pretendido
    }

    override fun opcao3() {
        // trocar o fragmebnto pretendido
    }

    override fun opcao4() {
        // trocar o fragmebnto pretendido
    }
}