package com.example.appmovel_pis.ui.menu

import DadosSensorFragment
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.DadosAreaFragment
import com.example.appmovel_pis.ui.fragments.InformationFragment
import com.example.appmovel_pis.ui.fragments.ProfileFragment
import com.example.appmovel_pis.ui.fragments.SensorFragment
import com.example.appmovel_pis.ui.objects.ChangeFragment
import com.example.appmovel_pis.ui.objects.RoleChecker

class MenuPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        // Procura os componentes pelo seu ID
        val iconSensor = findViewById<ImageView>(R.id.iconSensorImageView)
        val iconInstall = findViewById<ImageView>(R.id.iconInstallImageView)
        val iconProfile = findViewById<ImageView>(R.id.iconProfileImageView)
        val iconInformation = findViewById<ImageView>(R.id.iconInformationImageView)



        // Verifica se o FragmentManager está vazio
        if (savedInstanceState == null) {
            val sensorFragment = SensorFragment()
            iconSensor.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, sensorFragment) // Use replace or add
                .commit()
        }

        iconSensor.setOnClickListener {
            animateIcon(iconSensor) // Animate the clicked icon
            switchFragment(SensorFragment()) // Switch to SensorFragment
            highlightSelectedIcon(iconSensor)
        }

        iconInstall.setOnClickListener {
            animateIcon(iconInstall)
            switchFragment(DadosAreaFragment())
            highlightSelectedIcon(iconInstall)
        }

        iconProfile.setOnClickListener {
            animateIcon(iconProfile)
            switchFragment(ProfileFragment())
            highlightSelectedIcon(iconProfile)
        }

        iconInformation.setOnClickListener {
            animateIcon(iconInformation)
            switchFragment(InformationFragment())
            highlightSelectedIcon(iconInformation)
        }

        // ????
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun highlightSelectedIcon(selectedIcon: ImageView) {
        val iconSensor = findViewById<ImageView>(R.id.iconSensorImageView)
        val iconInstall = findViewById<ImageView>(R.id.iconInstallImageView)
        val iconProfile = findViewById<ImageView>(R.id.iconProfileImageView)
        val iconInformation = findViewById<ImageView>(R.id.iconInformationImageView)

        val icons = listOf(iconSensor, iconInstall, iconProfile, iconInformation)
        icons.forEach { icon ->
            if (icon == selectedIcon) {
                icon.setBackgroundResource(R.drawable.circule_background) // Use a drawable
                icon.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(200) // Animation duration in milliseconds
                    .start()
            } else {
                icon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(200) // Animation duration in milliseconds
                    .start()
            }
        }
    }

    private fun animateIcon(icon: ImageView) {
        // Scale up and then back down
        icon.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(200)
            .withEndAction {
                icon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    // Function to switch fragments
    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.menuFragmentContainer, fragment) // Replace the container ID with your fragment holder ID
        transaction.commit()
    }

    var remainingSensors: Int = 0

    // Function to load DadosSensorFragment
    fun showDadosSensorFragment() {
        val fragment = DadosSensorFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Called by DadosAreaFragment when "Adicionar Area" is clicked
    fun onAdicionarAreaClicked(sensorCount: Int) {
        // Set the number of sensors to create
        remainingSensors = sensorCount

        // Load the first DadosSensorFragment
        showDadosSensorFragment()
    }

    // Called by DadosSensorFragment when "Adicionar Sensor" is clicked
    fun onAdicionarSensorClicked() {

        remainingSensors--
        if (remainingSensors > 0) {
            // Show the next DadosSensorFragment
            showDadosSensorFragment()
        } else {
            // All sensors added, go back or show a confirmation
            Toast.makeText(this, "All sensors added!", Toast.LENGTH_SHORT).show()
            val dadosAreaFragmentFragment = DadosAreaFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, dadosAreaFragmentFragment) // Use replace or add
                .commit()
        }
    }
}