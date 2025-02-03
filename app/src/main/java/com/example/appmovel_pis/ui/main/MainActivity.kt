package com.example.appmovel_pis.ui.main

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.WelcomePageFragment
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the shared preference for dark mode
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        // Apply dark mode based on the stored preference
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Verifica se o FragmentManager está vazio
        if (savedInstanceState == null) {
            val welcomePageFragment = WelcomePageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, welcomePageFragment) // Use replace or add
                .commit()
        }
    }

    override fun onBackPressed() {
        // Create an AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to close the app?")
            .setPositiveButton("Yes") { _, _ ->
                // Exit the app
                exitProcess(0)
            }
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }
}