package com.example.appmovel_pis

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BaseDadosManager(private var context: Context) {
    suspend fun autenticar(email: String, senha: String): Utilizador? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, senha))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Utilizador(body.id, body.nome, body.email)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erro desconhecido na resposta do servidor.", Toast.LENGTH_SHORT).show()
                        }
                        null
                    }
                } else {
                    if (response.code() == 401) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Credenciais inválidas. Por favor, tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Exibe outros erros do servidor
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = errorBody ?: "Erro ao autenticar. Código HTTP: ${response.code()}"
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Exibe erro de exceção
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                null
            }
        }
    }
}

data class Utilizador(val id: Int, val nome: String, val email: String)
