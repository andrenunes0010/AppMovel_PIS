package com.example.appmovel_pis.ui.menu

import DadosSensorFragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.appmovel_pis.data.SessionManager
import com.example.appmovel_pis.ui.fragments.CriarUtilizadorFragment

class MenuPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        // Procura os componentes pelo seu ID
        val iconSensor = findViewById<ImageView>(R.id.iconSensorImageView)
        val iconInstall = findViewById<ImageView>(R.id.iconInstallImageView)
        val iconProfile = findViewById<ImageView>(R.id.iconProfileImageView)
        val iconInformation = findViewById<ImageView>(R.id.iconInformationImageView)

        // Initialize SessionManager
        val sessionManager = SessionManager(this)

        // Retrieve the user data
        val userData = sessionManager.getUser()

        // Check 'tipo' and adjust visibility
        userData?.let { user ->
            if (user.tipo == "utilizador") {
                // Change the icon for iconSensor
                iconSensor.setImageResource(R.drawable.ic_sensor)

                // Hide the iconInstall ImageView
                iconInstall.visibility = View.GONE
            } else if (user.tipo == "Técnico") {
                // Change the icon for iconSensor
                iconSensor.setImageResource(R.drawable.ic_adicionar)

                // Show the iconInstall ImageView
                iconInstall.visibility = View.VISIBLE
            }
        } ?: run {
            // Handle the case where user data is null (e.g., not logged in)
            iconInstall.visibility = View.GONE
        }


        // Verifica se o FragmentManager está vazio
        if (savedInstanceState == null) {
            val sensorFragment = SensorFragment()
            val adicionarFragment = CriarUtilizadorFragment()
            iconSensor.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
            userData?.let { user ->
                if (user.tipo == "utilizador") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menuFragmentContainer, sensorFragment) // Use replace or add
                        .commit()

                } else if (user.tipo == "Técnico") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menuFragmentContainer, adicionarFragment) // Use replace or add
                        .commit()
                } else {

                }
            }
        }

        iconSensor.setOnClickListener {
            // Retrieve the UserData from SessionManager
            val user = sessionManager.getUser() // Retrieve user data from SharedPreferences

            // Check if user data is not null and handle the "tipo" property
            user?.let {
                if (it.tipo == "utilizador") {
                    // Switch to a fragment relevant for "utilizador"
                    switchFragment(SensorFragment())
                } else if (it.tipo == "Técnico") {
                    // Switch to a fragment relevant for "Técnico"
                    switchFragment(CriarUtilizadorFragment())
                }

                // Highlight the selected icon
                highlightSelectedIcon(iconSensor)
            }
        }


        iconInstall.setOnClickListener {
            switchFragment(DadosAreaFragment())
            highlightSelectedIcon(iconInstall)
        }

        iconProfile.setOnClickListener {
            switchFragment(ProfileFragment())
            highlightSelectedIcon(iconProfile)
        }

        iconInformation.setOnClickListener {
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


    private fun highlightSelectedIcon(selectedIcon: ImageView) {
        val iconSensor = findViewById<ImageView>(R.id.iconSensorImageView)
        val iconInstall = findViewById<ImageView>(R.id.iconInstallImageView)
        val iconProfile = findViewById<ImageView>(R.id.iconProfileImageView)
        val iconInformation = findViewById<ImageView>(R.id.iconInformationImageView)

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val icons = listOf(iconSensor, iconInstall, iconProfile, iconInformation)
        icons.forEach { icon ->
            if (icon == selectedIcon) {
                icon.isClickable = false
                icon.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .translationYBy(-20f) // Move up slightly
                    .setDuration(200)
                    .withEndAction {
                        icon.animate()
                            .translationY(0f) // Bounce back
                            .setDuration(200)
                            .start()
                    }
                    .start()
            } else {
                icon.isClickable = true
                icon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .translationY(0f) // Reset position
                    .setDuration(200)
                    .start()
            }
        }
    }

   /* private fun animateIcon(icon: ImageView) {
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
    } */

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