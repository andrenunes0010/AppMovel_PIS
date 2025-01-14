package com.example.appmovel_pis.ui.menu

import android.media.Image
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        fun setupImageViewClickListener(
            imageView: ImageView,
            fragment: Fragment,
            scrollView: View,
            sizeChecker: View,
            fragmentContainerId: Int,
            fragmentManager: FragmentManager
        ) {
            imageView.setOnClickListener {
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

        val scrollView = findViewById<View>(R.id.scrollView)
        val sizeChecker = findViewById<ImageView>(R.id.sizeChecker)
        val fragmentManager = supportFragmentManager

        setupImageViewClickListener(
            imageView = findViewById(R.id.iconInformationImageView),
            fragment = InformationFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        setupImageViewClickListener(
            imageView = findViewById(R.id.iconProfileImageView),
            fragment = ProfileFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        setupImageViewClickListener(
            imageView = findViewById(R.id.iconSensorImageView),
            fragment = SensorFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        setupImageViewClickListener(
            imageView = findViewById(R.id.iconInstallImageView),
            fragment = InstallFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        // Check if the fragment is already added (e.g., after a configuration change)
        if (savedInstanceState == null) {
            val sensorFragment = SensorFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, sensorFragment) // Use replace or add
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun opcao1() {

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