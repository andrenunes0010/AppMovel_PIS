package com.example.appmovel_pis.ui.objects

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.appmovel_pis.ui.fragments.CriarUtilizadorFragment
import com.example.appmovel_pis.ui.fragments.SensorFragment

object RoleChecker {

    fun setupRoleCheckerClickListener(
        view: View,
        tipo: Int,
        scrollView: View,
        fragmentContainerId: Int,
        fragmentManager: FragmentManager
    ) {
        view.setOnClickListener {
            // Determine which fragment to load based on 'tipo'
            val fragment: Fragment = when (tipo) {
                1 -> SensorFragment() // Replace with your actual SensoresFragment class
                2 -> CriarUtilizadorFragment() // Replace with your actual CriarUtilizadorFragment class
                else -> throw IllegalArgumentException("Invalid fragment type") // Optional: handle invalid 'tipo'
            }

            // Start the fragment
            fragmentManager.beginTransaction()
                .replace(fragmentContainerId, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}