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
class DadosSensorFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
        fun newInstance(): DadosSensorFragment {
            return DadosSensorFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dados_sensor, container, false)

        val btnObterCoordenadas = view.findViewById<Button>(R.id.btnObterCoordenadas)
        val addButton = view.findViewById<Button>(R.id.btnSubmeter)
        latitudeTextView = view.findViewById(R.id.tvLatitude)
        longitudeTextView = view.findViewById(R.id.tvLongitude)

        addButton.setOnClickListener {
            // Notify MenuPage to show the next DadosSensorFragment or complete the process
            (activity as? MenuPage)?.onAdicionarSensorClicked()
        }

        // Configurar clique do botão para obter localização
        btnObterCoordenadas.setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }

        return view

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
    }
