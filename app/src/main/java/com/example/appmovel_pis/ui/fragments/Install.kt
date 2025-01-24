package com.example.appmovel_pis.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.data.SessionManager
import com.example.appmovel_pis.repository.BaseDadosManager
import com.example.appmovel_pis.ui.menu.MenuPage
import com.example.appmovel_pis.ui.objects.ClickAnimation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.location.LocationRequest

class InstallFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var dataIntalacaoTextView: TextView
    private lateinit var statusSensorTextView: TextView
    private lateinit var nomeSensorTextView: TextView
    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
    }


    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_install, container, false)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val btnSubmeter = view.findViewById<Button>(R.id.btnSubmeter)
        val btnObterMorada = view.findViewById<Button>(R.id.btnObterMorada)
        latitudeTextView = view.findViewById(R.id.tvLatitude)
        longitudeTextView = view.findViewById(R.id.tvLongitude)
        emailTextView = view.findViewById(R.id.et_owner_email)
        dataIntalacaoTextView = view.findViewById(R.id.et_installation_date)
        statusSensorTextView = view.findViewById(R.id.et_sensor_status)
        nomeSensorTextView = view.findViewById(R.id.et_sensor_name)*/

        // Inicializar o FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Aplicar animação no botão
        //ClickAnimation.applyTouchAnimation(btnSubmeter, requireContext())

        // Configurar clique do botão para obter localização
        /*btnObterMorada.setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }

        btnSubmeter.setOnClickListener {
            val email = emailTextView.text.toString()
            val Latitude = latitudeTextView.text.toString()
            val Longitude = longitudeTextView.text.toString()
            val DataInstalacao = dataIntalacaoTextView.text.toString()
            val Status = statusSensorTextView.text.toString()
            val NomeSensor = nomeSensorTextView.text.toString()
            //realizarInstalacaoSensor(email, Latitude, Longitude, DataInstalacao, Status, NomeSensor)
        }*/
    }

    private fun checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permissões
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1001
            )
        } else {
            // Obter localização
            getLocation()
        }
    }

    @SuppressLint("MissingPermission") // Supressão do aviso, pois as permissões são verificadas
    private fun getLocation() {
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Atualizar TextViews com latitude e longitude
                    val latitude = location.latitude
                    val longitude = location.longitude

                    latitudeTextView.text = "Latitude: $latitude"
                    longitudeTextView.text = "Longitude: $longitude"
                } else {
                    verificarLocalizacaoAtiva()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao obter localização: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            // Tratar o caso em que a permissão não foi concedida inesperadamente
            Toast.makeText(requireContext(), "Erro de permissão: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Tratar o resultado da solicitação de permissões
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarLocalizacaoAtiva() {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = com.google.android.gms.location.LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = com.google.android.gms.location.LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { response ->
            // Localização já está ativada
            Toast.makeText(requireContext(), "Localização está ativada!", Toast.LENGTH_SHORT).show()
        }

        task.addOnFailureListener { exception ->
            if (exception is com.google.android.gms.common.api.ResolvableApiException) {
                // Localização não está ativada, mas pode ser resolvida
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Erro ao tentar resolver
                    sendEx.printStackTrace()
                }
            } else {
                // Localização não pode ser ativada automaticamente
                Toast.makeText(requireContext(), "Ative a localização manualmente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    // O usuário ativou a localização
                    Toast.makeText(requireContext(), "Localização ativada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_CANCELED -> {
                    // O usuário cancelou a ativação
                    Toast.makeText(requireContext(), "Você precisa ativar a localização.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*private fun realizarInstalacaoSensor(
        email: String,
        Latitude: String,
        Longitude: String,
        DataInstalacao: String,
        Status: String,
        NomeSensor: String
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            // Crie a instância de SessionManager para recuperar o token
            val sessionManager = SessionManager(requireContext())  // Usando requireContext() em vez de this@InstallFragment
            val authManager = BaseDadosManager(requireContext())  // Usando requireContext()

            // Recupere o usuário autenticado e seu token
            val user = sessionManager.getUser()
            val token = user?.token

            if (token != null) {
                // Chame a função instal() para enviar os dados do sensor
                val sensorData = authManager.instal(
                    email,
                    Latitude,
                    Longitude,
                    DataInstalacao,
                    Status,
                    NomeSensor,
                    token
                )

                withContext(Dispatchers.Main) {
                    if (sensorData != null) {
                        // Se a instalação do sensor for bem-sucedida, mostre uma mensagem
                        Toast.makeText(requireContext(), "Sensor instalado com sucesso!", Toast.LENGTH_SHORT).show()

                        // Navegar para a próxima tela (se necessário)
                        val intent = Intent(requireActivity(), MenuPage::class.java)  // Usando requireActivity()
                        startActivity(intent)
                    } else {
                        // Se falhar, mostre uma mensagem de erro
                        Toast.makeText(requireContext(), "Erro ao instalar o sensor.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Caso o token não seja encontrado
                Toast.makeText(requireContext(), "Usuário não autenticado. Realize o login novamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}
