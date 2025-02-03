package com.example.appmovel_pis.ui.menu

import DadosSensorFragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import kotlin.system.exitProcess

class MenuPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_page)

        // Load the shared preference for dark mode
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        // Apply dark mode based on the stored preference
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Procura os componentes pelo seu ID
        val iconSensor = findViewById<ImageView>(R.id.iconSensorImageView)
        val iconInstall = findViewById<ImageView>(R.id.iconInstallImageView)
        val iconProfile = findViewById<ImageView>(R.id.iconProfileImageView)
        val iconInformation = findViewById<ImageView>(R.id.iconInformationImageView)

        // Busca o SessionManager e define-o
        val sessionManager = SessionManager(this)

        // Pega os dados do utilizador
        val userData = sessionManager.getUser()

        // Verifica o tipo que está guardado nos dados
        userData?.let { user ->
            if (user.tipo == "utilizador") {
                // Muda o icon
                iconSensor.setImageResource(R.drawable.ic_sensor)

                // Esconde o ImageView
                iconInstall.visibility = View.GONE
            } else if (user.tipo == "Técnico") {
                iconSensor.setImageResource(R.drawable.ic_adicionar)

                iconInstall.visibility = View.VISIBLE

            } else if (user.tipo == "Administrador") {
                iconSensor.setImageResource(R.drawable.ic_adicionar)

                iconInstall.visibility = View.VISIBLE
            }
        } ?: run {
            // Se estiver NULL esconde o ImageView
            iconSensor.setImageResource(R.drawable.ic_sensor)
            iconInstall.visibility = View.GONE
        }


        // Verifica se o FragmentManager está vazio
        if (savedInstanceState == null) {
            val sensorFragment = SensorFragment()
            val adicionarFragment = CriarUtilizadorFragment()
            iconSensor.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .start()

            userData?.let { user ->
                if (user.tipo == "utilizador") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menuFragmentContainer, sensorFragment)
                        .commit()

                } else if (user.tipo == "Técnico") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menuFragmentContainer, adicionarFragment)
                        .commit()
                } else if (user.tipo == "Administrador") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menuFragmentContainer, adicionarFragment)
                        .commit()
                } else {

                }
            }
        }

        iconSensor.setOnClickListener {
            // Pega os dados guardados no SessionManager
            val user = sessionManager.getUser()

            // Verifica que tipo de utilizador é e muda o Fragmento de acordo com isso
            user?.let {
                if (it.tipo == "utilizador") {
                    // muda o fragmento
                    switchFragment(SensorFragment())
                } else if (it.tipo == "Técnico") {
                    switchFragment(CriarUtilizadorFragment())

                } else if (it.tipo == "Administrador") {
                    switchFragment(CriarUtilizadorFragment())
                }

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
                exitProcess(0)
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
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .translationYBy(-20f) // Move up slightly
                    .setDuration(200)
                    .withEndAction {
                        // Shake animation
                        icon.animate()
                            .translationXBy(-10f) // Move left
                            .setDuration(50)
                            .withEndAction {
                                icon.animate()
                                    .translationXBy(20f) // Move right
                                    .setDuration(100)
                                    .withEndAction {
                                        icon.animate()
                                            .translationXBy(-10f) // Center back
                                            .setDuration(50)
                                            .withEndAction {
                                                icon.animate()
                                                    .translationY(0f) // Bounce back
                                                    .setDuration(200)
                                                    .start()
                                            }
                                            .start()
                                    }
                                    .start()
                            }
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

    // Função para alterar fragmentos com animação
    private fun switchFragment(fragment: Fragment) {
        val container = findViewById<ViewGroup>(R.id.menuFragmentContainer)

        // Slide the current fragment out of view by moving it down (fade-out effect)
        container.animate()
            .translationY(container.height.toFloat())  // Slide out to the bottom
            .alpha(0f)  // Fade out the current fragment
            .setDuration(250)
            .withEndAction {
                // After the first fragment slides out, replace the fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.menuFragmentContainer, fragment)
                transaction.commit()

                // Prepare the new fragment to slide in from the bottom (set translation to bottom)
                container.translationY = container.height.toFloat()  // Start from the bottom
                container.alpha = 0f  // Initially, make the fragment invisible
                container.animate()
                    .translationY(0f)  // Slide up to the original position
                    .alpha(1f)  // Fade in the new fragment
                    .setDuration(250)
                    .start()
            }
            .start()
    }

    private var currentSensorNumber: Int = 1
    private var remainingSensors: Int = 0

    // Função para determinar quantos sensores são
    fun onAdicionarAreaClicked(sensorCount: Int) {
        // Define o número de sensores
        remainingSensors = sensorCount
        currentSensorNumber = 1 // Reinicia o contador de sensores

        // Carrega a primeira instância para preencher
        showDadosSensorFragment()
    }

    // Função para determinar se todos os sensores já foram preenchidos
    fun onAdicionarSensorClicked() {
        remainingSensors--

        if (remainingSensors > 0) {
            // Mostra os próximos fragmentos
            currentSensorNumber++
            showDadosSensorFragment()
        } else {
            // Todos os sensores foram adicionados
            Toast.makeText(this, "Todos os sensores foram adicionados!", Toast.LENGTH_SHORT).show()
            val dadosAreaFragment = DadosAreaFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                .replace(R.id.menuFragmentContainer, dadosAreaFragment)
                .commit()
        }
    }

    // Função para carregar o fragmento de DadosSensor
    private fun showDadosSensorFragment() {
        val fragment = DadosSensorFragment.newInstance(currentSensorNumber)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.menuFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}