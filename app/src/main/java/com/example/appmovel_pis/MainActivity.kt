package com.example.appmovel_pis

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    val email = findViewById<EditText>(R.id.etEmail)
    val password = findViewById<EditText>(R.id.etPassword)
    val loginButton = findViewById<Button>(R.id.btnLogin)

    loginButton.setOnClickListener {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
            autenticarUtilizador(email, password)
        }
    }

    private fun autenticarUtilizador(email: String, senha: String) {
        val dbManager = BaseDadosManager()

        // Use uma thread separada para evitar bloquear a UI
        Thread {
            val utilizador = dbManager.getUtilizador(email, senha)

            runOnUiThread {
                if (utilizador != null) {
                    Toast.makeText(this, "Bem-vindo, ${utilizador.nome}!", Toast.LENGTH_SHORT).show()
                    // Aqui você pode navegar para outra atividade ou iniciar a sessão
                } else {
                    Toast.makeText(this, "Credenciais inválidas!", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}