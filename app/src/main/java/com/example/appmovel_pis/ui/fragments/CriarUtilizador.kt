package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.appmovel_pis.R
import com.example.appmovel_pis.repository.BaseDadosManager
import com.example.appmovel_pis.ui.objects.ClickAnimation
import kotlinx.coroutines.launch


class CriarUtilizadorFragment : Fragment() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmarPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_utilizador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNome = view.findViewById(R.id.etNome)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmarPassword = view.findViewById(R.id.etConfirmarPassword)
        val btnSubmeter = view.findViewById<Button>(R.id.btnSubmeter)
        ClickAnimation.applyTouchAnimation(btnSubmeter, requireContext())
        var itemSelecionado = ""

        val spinner = view.findViewById<Spinner>(R.id.mySpinner)
        val listaOpcoes = listOf("Administrador", "Técnico", "utilizador")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaOpcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSelecionado = listaOpcoes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada selecionado
            }
        }

        btnSubmeter.setOnClickListener{
            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmarPassword = etConfirmarPassword.text.toString()
            if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty() || itemSelecionado.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                if (password != confirmarPassword) {
                    Toast.makeText(requireContext(), "As passwords não coincidem!", Toast.LENGTH_SHORT).show()
                } else {
                    val baseDadosManager = BaseDadosManager(requireContext())

                    viewLifecycleOwner.lifecycleScope.launch {
                        val result = baseDadosManager.criarUtilizador(nome, email, password, itemSelecionado)
                        if (result != null) {
                            Toast.makeText(requireContext(), "Utilizador criado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Erro ao criar utilizador!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}