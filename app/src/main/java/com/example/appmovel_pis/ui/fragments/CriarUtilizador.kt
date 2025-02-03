package com.example.appmovel_pis.ui.fragments

import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
    private lateinit var etTelemovel: EditText

    class CustomSpinnerAdapter(
        context: Context,
        resource: Int,
        objects: List<String>,
        private val textColor: Int
    ) : ArrayAdapter<String>(context, resource, objects) {

        private val customFont = ResourcesCompat.getFont(context, R.font.poppins_medium) // Reference your custom font

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            val textView: TextView = view.findViewById(android.R.id.text1)

            textView.setTextColor(textColor)  // Set the selected item text color
            textView.typeface = customFont    // Set the custom font

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            val textView: TextView = view.findViewById(android.R.id.text1)

            textView.setTextColor(textColor)  // Set the dropdown item text color
            textView.typeface = customFont    // Set the custom font

            return view
        }
    }

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
        etTelemovel = view.findViewById(R.id.etTelemovel)
        val btnSubmeter = view.findViewById<Button>(R.id.btnSubmeter)
        ClickAnimation.applyTouchAnimation(btnSubmeter, requireContext())
        var itemSelecionado = ""

        val spinner = view.findViewById<Spinner>(R.id.mySpinner)
        val listaOpcoes = listOf("Administrador", "Técnico", "Utilizador")

        // Define the color you want for the spinner text
        val textColor = ContextCompat.getColor(
            requireContext(),
            R.color.writing
        ) // Replace with your desired color

        // Create the custom adapter
        val adapter = CustomSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaOpcoes,
            textColor
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                itemSelecionado = listaOpcoes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada selecionado
            }
        }

        btnSubmeter.setOnClickListener {
            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmarPassword = etConfirmarPassword.text.toString()
            val telemovel = etTelemovel.text.toString()

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty() || itemSelecionado.isEmpty() || telemovel.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT)
                    .show()
            } else if (!isValidPassword(password)) {
                Toast.makeText(
                    requireContext(),
                    "A senha deve ter pelo menos 6 caracteres e um caractere especial!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isValidPhoneNumber(telemovel)) {
                Toast.makeText(
                    requireContext(),
                    "Número de telemóvel inválido!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password == confirmarPassword) {
                val baseDadosManager = BaseDadosManager(requireContext())

                viewLifecycleOwner.lifecycleScope.launch {
                    val result = baseDadosManager.criarUtilizador(
                        nome,
                        email,
                        password,
                        itemSelecionado,
                        telemovel
                    )
                    if (result == null) {
                        Toast.makeText(
                            requireContext(),
                            "Utilizador criado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao criar utilizador!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "As passwords não coincidem!", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    fun isValidPassword(password: String): Boolean {
        val specialCharPattern = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")
        return password.length >= 6 && specialCharPattern.containsMatchIn(password)
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Expressão regular para validar um número de telefone com 9 dígitos
        val phonePattern = Regex("^[9][0-9]{8}$")
        return phonePattern.matches(phoneNumber)
    }
}
