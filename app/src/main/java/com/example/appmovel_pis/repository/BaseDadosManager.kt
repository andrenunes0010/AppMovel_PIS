package com.example.appmovel_pis.repository

import android.content.Context
import android.widget.Toast
import com.example.appmovel_pis.data.model.UserData
import com.example.appmovel_pis.data.network.LoginRequest
import com.example.appmovel_pis.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class BaseDadosManager(private var context: Context) {

    // Função para autenticar o usuário
    suspend fun autenticar(email: String, senha: String): UserData? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, senha))
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        // Login bem-sucedido
                        val data = apiResponse.data
                        if (data != null) {
                            Log.d("BaseDadosManager", "Token: ${data.token}")
                            UserData(data.id, data.nome, data.email, data.token)
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
                    // Aqui lidamos com o erro HTTP 401
                    if (response.code() == 401) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Credenciais inválidas. Por favor, tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Outros erros do servidor
                        val errorBody = response.errorBody()?.string()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erro no servidor: $errorBody", Toast.LENGTH_SHORT).show()
                        }
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