package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.menu.MenuPage

class DadosSensorFragment : Fragment() {

    companion object {
        fun newInstance(): DadosSensorFragment {
            return DadosSensorFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dados_sensor, container, false)

        val addButton = view.findViewById<Button>(R.id.btnSubmeter)

        addButton.setOnClickListener {
            // Notify MenuPage to show the next DadosSensorFragment or complete the process
            (activity as? MenuPage)?.onAdicionarSensorClicked()
        }

        return view
    }
}
