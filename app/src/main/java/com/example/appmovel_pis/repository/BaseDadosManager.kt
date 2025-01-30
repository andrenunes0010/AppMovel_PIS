package com.example.appmovel_pis.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appmovel_pis.data.model.UserData
import com.example.appmovel_pis.data.network.LoginRequest
import com.example.appmovel_pis.data.network.RetrofitClient
import com.example.appmovel_pis.utils.EncryptionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.appmovel_pis.data.SessionManager
import com.example.appmovel_pis.data.network.EncryptedRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import android.util.Base64
import com.example.appmovel_pis.data.model.AreaData
import com.example.appmovel_pis.data.network.InstallAreaRequest
import com.example.appmovel_pis.data.network.mudarPassword

class BaseDadosManager(private var context: Context) {

    private val encryptionUtils = EncryptionUtils("jwt_secret")

    suspend fun autenticar(email: String, senha: String, context: Context): UserData? {
        val sessionManager = SessionManager(context)

        return withContext(Dispatchers.IO) {
            try {
                // Encriptar os dados de login
                val encryptedData =
                    encryptionUtils.encryptAES(Json.encodeToString(LoginRequest(email, senha)))
                val encryptedRequest = EncryptedRequest(encryptedData)

                Log.d("Autenticar", "Dados encriptados: $encryptedData")

                // Enviar a requisição com dados encriptados
                val response = RetrofitClient.apiService(context).login(encryptedRequest)

                Log.d("Autenticar", "Resposta do servidor: ${response.body()}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null && apiResponse.success) {
                        // Validar os dados recebidos antes de tentar descriptografar
                        val encryptedResponseData = apiResponse.data?.toString() ?: ""
                        Log.d(
                            "Autenticar",
                            "Dados encriptados recebidos do servidor: $encryptedResponseData"
                        )

                        if (encryptedResponseData.isBlank() || !isBase64(encryptedResponseData)) {
                            Log.e(
                                "Autenticar",
                                "Os dados recebidos são inválidos para descriptografia!"
                            )
                            return@withContext null
                        }

                        // Descriptografar os dados recebidos
                        val decryptedData = encryptionUtils.decryptAES(encryptedResponseData)
                        Log.d("Autenticar", "Dados descriptografados: $decryptedData")

                        // Criar objeto UserData
                        val userData = Json.decodeFromString<UserData>(decryptedData)

                        // Validar e extrair o tipo do token JWT usando EncryptionUtils
                        if (userData.token != null && encryptionUtils.validateToken(userData.token)) {
                            val payload = encryptionUtils.extractPayload(userData.token)
                            userData.tipo =
                                payload["tipo"] // Atualiza o campo 'tipo' com o valor extraído do token
                        } else {
                            Log.e("Autenticar", "Token inválido ou nulo!")
                        }

                        // Salvar o usuário na sessão
                        sessionManager.saveUser(userData)

                        return@withContext userData
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                apiResponse?.message ?: "Erro desconhecido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@withContext null
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Credenciais inválidas. Por favor, tente novamente."
                        else -> response.errorBody()?.string() ?: "Erro desconhecido do servidor."
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    return@withContext null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
                return@withContext null
            }
        }
    }

    // Função para validar se uma string está em Base64
    private fun isBase64(data: String): Boolean {
        return try {
            Base64.decode(data, Base64.DEFAULT)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    suspend fun alterarPassword(currentPassword: String, newPassword: String): Boolean {
        val sessionManager = SessionManager(context)

        return withContext(Dispatchers.IO) {
            try {
                val user = sessionManager.getUser() // Recupera os dados do usuário logado

                // Encriptar os dados para a API
                val encryptedData = encryptionUtils.encryptAES(
                    Json.encodeToString(mudarPassword(currentPassword, newPassword))
                )
                val encryptedRequest = EncryptedRequest(encryptedData)

                // Fazer a chamada à API
                val response = RetrofitClient.apiService(context).mudarPassword(encryptedRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        // Password alterada com sucesso
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Password alterada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@withContext true
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                apiResponse?.message ?: "Erro ao alterar a password.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@withContext false
                    }
                } else {
                    Log.e("Autenticar", "Resposta do servidor: ${response.errorBody()?.string()}")
                    val errorMessage = when (response.code()) {
                        401 -> "Token inválido ou sessão expirada."
                        else -> response.errorBody()?.string() ?: "Erro desconhecido do servidor."
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    return@withContext false

                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
                return@withContext false
            }
        }
    }

    suspend fun installArea(
        nome: String,
        tamanho: String,
        email: String,
        quantidadeConjuntos: Int,
        latitude: String,
        longitude: String
    ): AreaData? {
        val sessionManager = SessionManager(context)
        return withContext(Dispatchers.IO) {
            try {
                val user = sessionManager.getUser()
                val token = user?.token
                if (token == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erro: Token não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                    return@withContext null
                }

                val response = RetrofitClient.apiService(context).installArea(
                    InstallAreaRequest(
                        nome = nome,
                        tamanho = tamanho,
                        email = email,
                        quantidadeConjuntos = quantidadeConjuntos,
                        latitude = latitude,
                        longitude = longitude
                    )
                )

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null && apiResponse.success) {
                        val encryptedData = apiResponse.data?.toString() // Garante que seja uma String

                        // Log para verificar os dados recebidos
                        Log.d("installArea", "Resposta da API: $apiResponse")
                        Log.d("installArea", "Dados encriptados recebidos: $encryptedData")

                        // Verificar se o campo `data` não é nulo ou vazio
                        if (encryptedData.isNullOrBlank()) {
                            Log.e("installArea", "Os dados recebidos são inválidos para descriptografia!")
                            return@withContext null
                        }

                        // Descriptografar os dados recebidos
                        val decryptedData = encryptionUtils.decryptAES(encryptedData)

                        // Criar objeto AreaData a partir dos dados descriptografados
                        return@withContext Json.decodeFromString<AreaData>(decryptedData)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                apiResponse?.message ?: "Erro ao instalar a área.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@withContext null
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Token inválido ou não autorizado."
                        else -> response.errorBody()?.string() ?: "Erro desconhecido do servidor."
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    return@withContext null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                return@withContext null
            }
        }
    }



    /*suspend fun installSensor(
        email: String,
        Latitude: String,
        Longitude: String,
        DataInstalacao: String,
        Status: String,
        NomeSensor: String,
        token: String
    ): SensorData? {
        val sessionManager = SessionManager(context)

        return withContext(Dispatchers.IO) {
            try {
                val user = sessionManager.getUser()
                val token = user?.token
                if (token == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erro: Token não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                    return@withContext null
                }

                val response = RetrofitClient.apiService.install(
                    InstallRequest(
                        email = email,
                        Latitude = Latitude,
                        Longitude = Longitude,
                        DataInstalacao = DataInstalacao,
                        Status = Status,
                        NomeSensor = NomeSensor,
                        token = token
                    )
                )

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        return@withContext apiResponse.data
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                apiResponse?.message ?: "Erro ao instalar o sensor.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        null
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Token inválido ou não autorizado."
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
    }*/
}