package com.example.appmovel_pis.repository

import android.content.Context
import android.widget.Toast
import com.example.appmovel_pis.data.network.LoginRequest
import com.example.appmovel_pis.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BaseDadosManager(private var context: Context) {
    suspend fun autenticar(email: String, senha: String): Utilizador? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, senha))
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        // Login bem-sucedido
                        val data = apiResponse.data
                        if (data != null) {
                            Utilizador(data.id, data.nome, data.email)
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Erro: dados ausentes na resposta.", Toast.LENGTH_SHORT).show()
                            }
                            null
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, apiResponse?.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                        }
                        null
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erro no servidor: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                null
            }
        }
    }
}

// Classe para representar o usuário
data class Utilizador(val id: Int, val nome: String, val email: String)