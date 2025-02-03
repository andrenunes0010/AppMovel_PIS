package com.example.appmovel_pis.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.location.Geocoder
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.appmovel_pis.R
import kotlinx.coroutines.launch
import com.example.appmovel_pis.repository.BaseDadosManager
import android.location.Address
import java.util.Locale

class SensorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sensores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tableLayout = view.findViewById<TableLayout>(R.id.tlSensores)

        viewLifecycleOwner.lifecycleScope.launch {
            val baseDadosManager = BaseDadosManager(requireContext())
            val areas = baseDadosManager.getAreas()

            if (areas != null) {
                for (area in areas) {
                    val tableRow = TableRow(requireContext())

                    // Create TextView for Sensor Name
                    val nomeTextView = TextView(requireContext()).apply {
                        text = area.nome
                        setPadding(8, 8, 8, 8)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins_medium))
                    }

                    // Create TextView for Status (Visual Indicator)
                    val statusTextView = TextView(requireContext()).apply {
                        text = if (area.status == "Ativo") "🟢" else "🔴"
                        setPadding(8, 8, 8, 8)
                        gravity = Gravity.CENTER
                    }

                    // Get location name from latitude & longitude
                    val localizacao = getNomeCidade(area.latitude, area.longitude)

                    // Create TextView for Location
                    val localizacaoTextView = TextView(requireContext()).apply {
                        text = localizacao
                        setPadding(8, 8, 8, 8)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        textSize = 16f
                        gravity = Gravity.CENTER
                    }

                    // Add views to TableRow
                    tableRow.addView(nomeTextView)
                    tableRow.addView(statusTextView)
                    tableRow.addView(localizacaoTextView)

                    // Make row clickable
                    tableRow.setOnClickListener {
                        showSensorPopup(area.nome, area.status, localizacao)
                    }

                    // Add TableRow to TableLayout
                    tableLayout.addView(tableRow)
                }
            } else {
                Toast.makeText(requireContext(), "Erro ao obter áreas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to show popup dialog
    @SuppressLint("MissingInflatedId")
    private fun showSensorPopup(nome: String, status: String, localizacao: String) {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_sensor_info, null)

        view.findViewById<TextView>(R.id.sensorIdText).text = "ID: $nome"
        view.findViewById<TextView>(R.id.sensorStateText).text = "Estado: $status"
        view.findViewById<TextView>(R.id.sensorLocationText).text = "Localização: $localizacao"

        dialog.setContentView(view)
        dialog.show()
    }

    // Function to get city name from latitude & longitude
    private fun getNomeCidade(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                return addresses[0].locality ?: "Localização desconhecida"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Localização desconhecida"
    }
}
