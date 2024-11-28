package com.example.appmovel_pis

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthManager {
    suspend fun autenticar(email: String, senha: String): Utilizador? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, senha))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Utilizador(body.id, body.nome, body.email)
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

data class Utilizador(val id: Int, val nome: String, val email: String)