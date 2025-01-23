package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.menu.MenuPage


class DadosAreaFragment : Fragment() {

    companion object {
        fun newInstance(): DadosAreaFragment {
            return DadosAreaFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dados_area, container, false)

        val editTextQuantidade = view.findViewById<EditText>(R.id.etConjuntos)
        val addButton = view.findViewById<Button>(R.id.btnSubmeter)

        addButton.setOnClickListener {
            val inputText = editTextQuantidade.text.toString()
            if (inputText.isNotEmpty()) {
                val sensorCount = inputText.toIntOrNull()
                if (sensorCount != null && sensorCount > 0) {
                    // Notify MenuPage to start DadosSensorFragment
                    (activity as? MenuPage)?.onAdicionarAreaClicked(sensorCount)
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}