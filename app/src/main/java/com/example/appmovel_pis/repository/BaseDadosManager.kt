package com.example.appmovel_pis.repository

import android.content.Context
import android.widget.Toast
import com.example.appmovel_pis.data.model.UserData
import com.example.appmovel_pis.data.network.LoginRequest
import com.example.appmovel_pis.data.network.RetrofitClient
import com.example.appmovel_pis.utils.EncryptionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class BaseDadosManager(private var context: Context) {

    private val encryptionUtils = EncryptionUtils("jwt_secret")

    suspend fun autenticar(email: String, senha: String): UserData? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, senha))
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        val data = apiResponse.data
                        if (data != null) {
                            val token = data.token
                            Log.d("BaseDadosManager", "Token recebido: $token")

                            // Valida o token
                            if (encryptionUtils.validateToken(token)) {
                                val payload = encryptionUtils.extractPayload(token)
                                val userTipo = payload["tipo"]
                                Log.d("BaseDadosManager", "Tipo do usuário: $payload")

                                // Retorna os dados do usuário
                                return@withContext UserData(data.id, data.nome, data.email, token, tipo = userTipo ?: "Desconhecido")
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Token inválido ou expirado.", Toast.LENGTH_SHORT).show()
                                }
                                null
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Erro: Dados ausentes na resposta.", Toast.LENGTH_SHORT).show()
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
                    // Lida com erros HTTP
                    val errorMessage = when (response.code()) {
                        401 -> "Credenciais inválidas. Por favor, tente novamente."
                        else -> response.errorBody()?.string() ?: "Erro desconhecido do servidor."
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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