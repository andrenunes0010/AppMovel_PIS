package com.example.appmovel_pis.ui.fragments

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

        // Inflate the layout for this fragment
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

                    // Criar TextView para o Nome da Área
                    val nomeTextView = TextView(requireContext()).apply {
                        text = area.nome
                        setPadding(8, 8, 8, 8)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins_medium))
                    }

                    // Criar TextView para o Status (indicador visual)
                    val statusTextView = TextView(requireContext()).apply {
                        text = if (area.status == "Ativo") "🟢" else "🔴"
                        setPadding(8, 8, 8, 8)
                        gravity = Gravity.CENTER
                    }

                    // Aqui, pegamos a latitude e longitude da área
                    val latitude = area.latitude // Supondo que 'latitude' seja um campo da sua classe AreaData
                    val longitude = area.longitude // Supondo que 'longitude' seja um campo da sua classe AreaData

                    // Obter o nome da cidade com base nas coordenadas
                    val localizacao = getNomeCidade(latitude, longitude)

                    // Criar TextView para a Localização
                    val localizacaoTextView = TextView(requireContext()).apply {
                        text = localizacao
                        setPadding(8, 8, 8, 8)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        textSize = 16f
                        gravity = Gravity.CENTER
                    }

                    // Adicionar os TextViews à TableRow
                    tableRow.addView(nomeTextView)
                    tableRow.addView(statusTextView)
                    tableRow.addView(localizacaoTextView)

                    // Adicionar TableRow ao TableLayout
                    tableLayout.addView(tableRow)
                }
            } else {
                Toast.makeText(requireContext(), "Erro ao obter áreas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para obter o nome da cidade a partir de latitude e longitude
    fun getNomeCidade(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            // Realiza a busca reversa para obter o endereço
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                // Aqui você pode retornar o nome da cidade
                return address.locality ?: "Localização desconhecida"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Localização desconhecida"
    }
}