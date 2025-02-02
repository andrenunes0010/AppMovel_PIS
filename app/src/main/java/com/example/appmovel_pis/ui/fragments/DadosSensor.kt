import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
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
import androidx.lifecycle.lifecycleScope
import com.example.appmovel_pis.R
import com.example.appmovel_pis.repository.BaseDadosManager
import com.google.android.gms.location.*
import com.example.appmovel_pis.ui.menu.MenuPage
import com.example.appmovel_pis.ui.objects.ClickAnimation
import kotlinx.coroutines.launch


class DadosSensorFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
        private const val SENSOR_NUMBER_KEY = "sensor_number"
        fun newInstance(sensorNumber: Int): DadosSensorFragment {
            val fragment = DadosSensorFragment()
            val args = Bundle()
            args.putInt(SENSOR_NUMBER_KEY, sensorNumber)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dados_sensor, container, false)

        val sensorNumber = arguments?.getInt(SENSOR_NUMBER_KEY) ?: 1
        val titleTextView = view.findViewById<TextView>(R.id.tvSensorDados)
        titleTextView.text = "Dados do Sensor #$sensorNumber"

        val addSensorButton = view.findViewById<Button>(R.id.btnSubmeter)
        addSensorButton.setOnClickListener {
            val latitude = tvLatitude.text.toString()
            val longitude = tvLongitude.text.toString()

            if (latitude.isBlank() || longitude.isBlank()) {
                Toast.makeText(requireContext(), "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
            } else {
                val baseDadosManager = BaseDadosManager(requireContext())

                viewLifecycleOwner.lifecycleScope.launch {
                    val sensorData = baseDadosManager.installConjunto(
                        Latitude = latitude,
                        Longitude = longitude
                    )
                    if (sensorData != null){
                        Toast.makeText(requireContext(), "Sensor adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                        (activity as? MenuPage)?.onAdicionarSensorClicked()
                    } else {
                        Toast.makeText(requireContext(), "Erro ao adicionar sensor.", Toast.LENGTH_SHORT).show()
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

        btnSubmeter.setOnClickListener {
            val latitude = tvLatitude.text.toString()
            val longitude = tvLongitude.text.toString()
            if (latitude.isBlank() || longitude.isBlank()) {
                Toast.makeText(requireContext(), "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
            } else {
                val baseDadosManager = BaseDadosManager(requireContext())
                viewLifecycleOwner.lifecycleScope.launch {
                    val sensorData = baseDadosManager.installConjunto(
                        Latitude = latitude,
                        Longitude = longitude
                    )
                    if (sensorData != null){
                        Toast.makeText(requireContext(), "Sensor adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                        (activity as? MenuPage)?.onAdicionarSensorClicked()
                    } else {
                        Toast.makeText(requireContext(), "Erro ao adicionar sensor.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        ClickAnimation.applyTouchAnimation(btnObterCoordenadas, requireContext())
        ClickAnimation.applyTouchAnimation(btnSubmeter, requireContext())
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
                        DadosSensorFragment.REQUEST_CHECK_SETTINGS
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
