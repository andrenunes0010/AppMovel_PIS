package com.example.appmovel_pis.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.WelcomePageFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verifica se o FragmentManager está vazio
        if (savedInstanceState == null) {
            val welcomePageFragment = WelcomePageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, welcomePageFragment) // Use replace or add
                .commit()
        }
    }
}