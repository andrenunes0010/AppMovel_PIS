package com.example.appmovel_pis.ui.objects

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object ScrollViewFuntion {

    fun setupImageViewClickListener(
        view: View,
        fragment: Fragment,
        scrollView: View,
        fragmentContainerId: Int,
        fragmentManager: FragmentManager
    ) {
        view.setOnClickListener {
            // Começa o Fragmento
            fragmentManager.beginTransaction()
                .replace(fragmentContainerId, fragment)
                .addToBackStack(null)
                .commit()

            // Ajusta o tamanho da ScrollView dependendo se a informação dentro do fragmento é ultrapassa o tamanho desejável

        }
    }
}