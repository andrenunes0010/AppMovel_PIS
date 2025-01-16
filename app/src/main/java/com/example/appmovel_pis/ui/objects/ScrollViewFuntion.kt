package com.example.appmovel_pis.ui.objects

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.ViewTreeObserver
import androidx.core.view.updateLayoutParams

object ScrollViewFuntion {

    fun setupImageViewClickListener(
        view: View,
        fragment: Fragment,
        scrollView: View,
        sizeChecker: View,
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
            scrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val sizeCheckerLocation = IntArray(2)
                    sizeChecker.getLocationOnScreen(sizeCheckerLocation)
                    val scrollViewLocation = IntArray(2)
                    scrollView.getLocationOnScreen(scrollViewLocation)

                    // Calcula se o sizeChecker está visivel na tela ou não
                    val isSizeCheckerVisible = sizeCheckerLocation[1] > scrollViewLocation[1] + scrollView.height

                    if (!isSizeCheckerVisible) {
                        // Ajusta o tamanho do ScrollView para 0
                        scrollView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            height = 0
                        }
                    } else {
                        // Ajusta o tamanho do ScrollView para "wrap_content"
                        scrollView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                        }
                    }

                    // Remove o "Listener" para evitar repetições de ajustamentos
                    scrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }
}