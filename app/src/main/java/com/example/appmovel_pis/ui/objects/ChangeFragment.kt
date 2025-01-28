package com.example.appmovel_pis.ui.objects


import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object ChangeFragment {

    fun setupImageViewClickListener(
        view: View,
        fragment: Fragment,
        fragmentContainerId: Int,
        fragmentManager: FragmentManager,
        enterAnimation: Int, // Animation resource for fragment entering
        exitAnimation: Int,  // Animation resource for fragment exiting
        popEnterAnimation: Int, // Animation resource for fragment re-entering (pop)
        popExitAnimation: Int // Animation resource for fragment popping out
    ) {
        view.setOnClickListener {
            // Start the fragment transaction with animations
            fragmentManager.beginTransaction()
                .setCustomAnimations(
                    enterAnimation,  // Enter animation for the new fragment
                    exitAnimation,   // Exit animation for the current fragment
                    popEnterAnimation, // Enter animation when popping back
                    popExitAnimation // Exit animation when popping back
                )
                .replace(fragmentContainerId, fragment)
                .addToBackStack(null)
                .commit()

            // You can add additional logic here to adjust the ScrollView size if needed
        }
    }
}
