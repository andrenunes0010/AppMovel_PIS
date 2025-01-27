package com.example.appmovel_pis.ui.main

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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

    override fun onBackPressed() {
        // Create an AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to close the app?")
            .setPositiveButton("Yes") { _, _ ->
                // Exit the app
                System.exit(0)
            }
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }
}