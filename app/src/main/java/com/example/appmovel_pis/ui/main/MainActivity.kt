package com.example.appmovel_pis.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmovel_pis.R
import com.example.appmovel_pis.repository.BaseDadosManager
import com.example.appmovel_pis.ui.menu.MenuPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {

            val email = emailEditText.text.toString()
            val senha = passwordEditText.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                realizarLogin(email, senha)
            }
        }
    }

    private fun realizarLogin(email: String, senha: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val authManager = BaseDadosManager(this@MainActivity)
            val utilizador = authManager.autenticar(email, senha)

            withContext(Dispatchers.Main) {
                if (utilizador != null) {
                    Toast.makeText(this@MainActivity, "Bem-vindo, ${utilizador.nome}!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, MenuPage::class.java)
                    startActivity(intent)
                } else {
                    null
                }
            }
        }
    }
}
