package com.example.appmovel_pis.ui.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.fragments.DadosAreaFragment
import com.example.appmovel_pis.ui.fragments.DadosSensorFragment
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
        val scrollView = findViewById<View>(R.id.scrollView)
        val fragmentManager = supportFragmentManager
        val tipo = 1

        // Define a função do ScrollView para a aba de Informação
        ChangeFragment.setupImageViewClickListener(
            view = findViewById(R.id.iconInformationImageView),
            fragment = InformationFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        // Define a função do ScrollView para a aba de Perfil
        ChangeFragment.setupImageViewClickListener(
            view = findViewById(R.id.iconProfileImageView),
            fragment = ProfileFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        RoleChecker.setupRoleCheckerClickListener(
            view = findViewById(R.id.iconSensorImageView), // Replace with your actual button or clickable view
            tipo = tipo,
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = supportFragmentManager
        )

        // Define a função do ScrollView para a aba de Instalações
        ChangeFragment.setupImageViewClickListener(
            view = findViewById(R.id.iconInstallImageView),
            fragment = DadosAreaFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        // Verifica se o FragmentManager está vazio
        if (savedInstanceState == null) {
            val sensorFragment = SensorFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.menuFragmentContainer, sensorFragment) // Use replace or add
                .commit()
        }

        // ????
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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