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
            // Load the Fragment
            fragmentManager.beginTransaction()
                .replace(fragmentContainerId, fragment)
                .addToBackStack(null)
                .commit()

            // Adjust ScrollView height dynamically
            scrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val footerDividerLocation = IntArray(2)
                    sizeChecker.getLocationOnScreen(footerDividerLocation)
                    val scrollViewLocation = IntArray(2)
                    scrollView.getLocationOnScreen(scrollViewLocation)

                    // Calculate visibility of footerDivider
                    val isFooterDividerVisible = footerDividerLocation[1] > scrollViewLocation[1] + scrollView.height

                    if (!isFooterDividerVisible) {
                        // Adjust ScrollView height to 0dp (MATCH_CONSTRAINT)
                        scrollView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            height = 0
                        }
                    } else {
                        // Reset ScrollView height to wrap_content
                        scrollView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                        }
                    }

                    // Remove listener to avoid repeated adjustments
                    scrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }
}