package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ClickAnimation


class CriarUtilizadorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_utilizador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSubmeter = view.findViewById<Button>(R.id.btnSubmeter)
        ClickAnimation.applyTouchAnimation(btnSubmeter, requireContext())

        val spinner = view.findViewById<Spinner>(R.id.mySpinner)
        val listaOpcoes = listOf("Administrador", "Técnico", "utilizador")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaOpcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemSelecionado = listaOpcoes[position]
                Toast.makeText(requireContext(), "Selecionado: $itemSelecionado", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada selecionado
            }
        }
    }

}