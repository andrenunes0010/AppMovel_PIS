package com.example.appmovel_pis.ui.menu

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.InformationFragment
import com.example.appmovel_pis.ui.fragments.InstallFragment
import com.example.appmovel_pis.ui.fragments.ProfileFragment
import com.example.appmovel_pis.ui.fragments.SensorFragment
import com.example.appmovel_pis.ui.objects.ScrollViewFuntion

class MenuPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        val scrollView = findViewById<View>(R.id.scrollView)
        val sizeChecker = findViewById<ImageView>(R.id.sizeChecker)
        val fragmentManager = supportFragmentManager

        ScrollViewFuntion.setupImageViewClickListener(
            view = findViewById(R.id.iconInformationImageView),
            fragment = InformationFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ScrollViewFuntion.setupImageViewClickListener(
            view = findViewById(R.id.iconProfileImageView),
            fragment = ProfileFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ScrollViewFuntion.setupImageViewClickListener(
            view = findViewById(R.id.iconSensorImageView),
            fragment = SensorFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ScrollViewFuntion.setupImageViewClickListener(
            view = findViewById(R.id.iconInstallImageView),
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
}