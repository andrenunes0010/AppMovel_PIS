package com.example.appmovel_pis.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.data.SessionManager
import com.example.appmovel_pis.repository.BaseDadosManager
import com.example.appmovel_pis.ui.menu.MenuPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.appmovel_pis.ui.objects.ChangeFragment
import com.example.appmovel_pis.ui.objects.ClickAnimation

class LoginFragment : Fragment() {

    private var currentToast: Toast? = null // Variable to keep track of the current toast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailEditText = view.findViewById<EditText>(R.id.etEmail)
        val passwordEditText = view.findViewById<EditText>(R.id.etPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = passwordEditText.text.toString()

            // Hide the keyboard
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            if (email.isEmpty() || senha.isEmpty()) {
                showToast("Preencha todos os campos!")
            } else {
                realizarLogin(email, senha)
            }
        }

        val goBackBTN = view.findViewById<LinearLayout>(R.id.goBackBTN)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val fragmentManager = requireActivity().supportFragmentManager
        val forgot = view.findViewById<TextView>(R.id.forgot_password)

        // Define a função do ScrollView para a aba de Definições
        ChangeFragment.setupImageViewClickListener(
            view = goBackBTN,
            fragment = WelcomePageFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        forgot.setOnClickListener {
            val intent = Intent(requireContext(), MenuPage::class.java)
            startActivity(intent)
        }

        ClickAnimation.applyTouchAnimation(goBackBTN, requireContext())

        return view
    }

    private fun realizarLogin(email: String, senha: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val sessionManager = SessionManager(requireContext())
            val authManager = BaseDadosManager(requireContext())

            val utilizador = authManager.autenticar(email, senha, requireContext())

            withContext(Dispatchers.Main) {
                if (utilizador != null) {
                    sessionManager.saveUser(utilizador)
                    showToast("Bem-vindo, ${utilizador.nome}!")

                    val intent = Intent(requireContext(), MenuPage::class.java)
                    startActivity(intent)
                } else {
                    showToast("Erro ao realizar login")
                }
            }
        }
    }

    private fun showToast(message: String) {
        // Cancel the current toast if it exists
        currentToast?.cancel()

        // Create and show a new toast
        currentToast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}