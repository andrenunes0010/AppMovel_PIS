package com.example.appmovel_pis.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.menu.MenuPage
import com.example.appmovel_pis.ui.objects.ClickAnimation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import com.example.appmovel_pis.repository.BaseDadosManager



class DadosAreaFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var etNome: EditText
    private lateinit var etTamanho: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTipoArea: EditText

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
        private const val SENSOR_NUMBER_KEY = "sensor_number"
        fun newInstance(): DadosAreaFragment {
            return DadosAreaFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dados_area, container, false)

        etNome = view.findViewById<EditText>(R.id.etNome)
        etTamanho = view.findViewById<EditText>(R.id.etTamanho)
        etEmail = view.findViewById<EditText>(R.id.etEmail)
        etTipoArea = view.findViewById<EditText>(R.id.etTipoArea)
        val editTextQuantidade = view.findViewById<EditText>(R.id.etConjuntos)
        val addButton = view.findViewById<Button>(R.id.btnSubmeter)

        addButton.setOnClickListener {
            val nomeArea = etNome.text.toString()
            val tipoArea = etTipoArea.text.toString()
            val tamanhoArea = etTamanho.text.toString()
            val emailPessoalArea = etEmail.text.toString()
            val latitude = tvLatitude.text.toString()
            val longitude = tvLongitude.text.toString()
            val inputText = editTextQuantidade.text.toString()
            val sensorCount = inputText.toIntOrNull()

            // Verifica se todos os campos estão preenchidos
            if (nomeArea.isBlank() || tamanhoArea.isBlank() || emailPessoalArea.isBlank() ||
                latitude.isBlank() || longitude.isBlank() || inputText.isBlank() || tipoArea.isBlank()) {
                Toast.makeText(requireContext(), "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (sensorCount == null || sensorCount <= 0){
                Toast.makeText(requireContext(), "Por favor, insira um número válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                // Criar uma instância de BaseDadosManager
                val baseDadosManager = BaseDadosManager(requireContext())

                // Lançar coroutine para instalar a área
                viewLifecycleOwner.lifecycleScope.launch {
                    val areaData = baseDadosManager.installArea(
                        nome = nomeArea,
                        tamanho = tamanhoArea,
                        tipo_area = tipoArea,
                        email = emailPessoalArea,
                        quantidadeConjuntos = sensorCount,
                        latitude = latitude,
                        longitude = longitude
                    )

                    if (areaData != null) {
                        Toast.makeText(requireContext(), "Área instalada com sucesso!", Toast.LENGTH_SHORT).show()
                        (activity as? MenuPage)?.onAdicionarAreaClicked(sensorCount)
                    } else {
                        Toast.makeText(requireContext(), "Erro ao instalar a área.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLatitude = view.findViewById(R.id.tvLatitude)
        tvLongitude = view.findViewById(R.id.tvLongitude)
        val btnObterCoordenadas = view.findViewById<Button>(R.id.btnObterCoordenadas)
        val btnSubmeter = view.findViewById<Button>(R.id.btnSubmeter)

        // Inicializar o FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Configurar a ação do botão
        btnObterCoordenadas.setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }

        ClickAnimation.applyTouchAnimation(btnObterCoordenadas, requireContext())
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

                    tvLatitude.text = "Latitude: $latitude"
                    tvLongitude.text = "Longitude: $longitude"
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
                    exception.startResolutionForResult(requireActivity(),
                        DadosAreaFragment.REQUEST_CHECK_SETTINGS
                    )
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
}