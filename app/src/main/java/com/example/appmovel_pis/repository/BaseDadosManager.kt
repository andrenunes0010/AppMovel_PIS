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
import com.example.appmovel_pis.data.model.ApiResponse
import com.example.appmovel_pis.data.model.AreaData
import com.example.appmovel_pis.data.model.SensorData
import com.example.appmovel_pis.data.network.InstallAreaRequest
import com.example.appmovel_pis.data.network.InstallConjuntoRequest
import com.example.appmovel_pis.data.network.criarUtilizadorRequest
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

    suspend fun installArea(nome: String, tamanho: String, tipo_area: String , email: String, quantidadeConjuntos: Int, latitude: String, longitude: String): AreaData? {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = InstallAreaRequest(
                    nome = nome,
                    tamanho_hectares = tamanho,
                    tipo_area = tipo_area,
                    responsavel = email,
                    quantidades_conjuntos = quantidadeConjuntos,
                    latitude = latitude,
                    longitude = longitude
                )

                Log.d("installArea", "🔹 Dados originais da requisição: $requestData")

                val encryptedData = encryptionUtils.encryptAES(Json.encodeToString(requestData))
                val encryptedRequest = EncryptedRequest(encryptedData)

                Log.d("installArea", "🔹 Dados encriptados enviados: $encryptedData")

                val response = RetrofitClient.apiService(context).installArea(encryptedRequest)

                Log.d("installArea", "🔹 Código de resposta HTTP: ${response.code()}")

                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    Log.e("installArea", "⚠️ Erro HTTP ${response.code()}: $errorBody")
                    return@withContext null
                }

                val apiResponse = response.body()
                Log.d("installArea", "🔹 Corpo da resposta: $apiResponse")

                if (apiResponse == null || !apiResponse.success) {
                    Log.e("installArea", "⚠️ Erro: ${apiResponse?.message ?: "Erro ao instalar a área."}")
                    return@withContext null
                }

                val encryptedResponseData = apiResponse.data?.toString()
                Log.d("installArea", "🔹 Dados encriptados recebidos: $encryptedResponseData")

                if (encryptedResponseData.isNullOrBlank()) {
                    Log.e("installArea", "⚠️ Erro: Dados encriptados vazios ou inválidos!")
                    return@withContext null
                }

                val decryptedData = encryptionUtils.decryptAES(encryptedResponseData)
                Log.d("installArea", "🔹 Dados descriptografados: $decryptedData")

                return@withContext if (decryptedData.toIntOrNull() != null) {
                    val id = decryptedData.toInt()
                    Log.d("installArea", "⚠️ Resposta contém apenas um ID: $id")

                    // Criar um objeto AreaData com valores padrão
                    AreaManager.AreaCriada = AreaData(
                        id = id,
                        nome = "Desconhecido",
                        tamanho = "0",
                        email = "desconhecido@email.com",
                        status = "Desconhecido",
                        latitude = 0.0,
                        longitude = 0.0
                    )

                    AreaManager.AreaCriada
                } else {
                    // 🔹 Se for um JSON, desserializa normalmente
                    val areaData = Json.decodeFromString<AreaData>(decryptedData)
                    AreaManager.AreaCriada = areaData
                    areaData
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("installArea", "🚨 Erro inesperado: ${e.localizedMessage}")
                return@withContext null
            }
        }
    }

    suspend fun installConjunto(Latitude: String, Longitude: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val areaData = AreaManager.AreaCriada
                if (areaData == null) {
                    Log.e("installConjunto", "⚠️ Nenhuma área foi encontrada!")
                    return@withContext false
                }

                val requestData = InstallConjuntoRequest(
                    area_id = areaData.id,
                    latitude = Latitude,
                    longitude = Longitude
                )

                val encryptedData = encryptionUtils.encryptAES(Json.encodeToString(requestData))
                val encryptedRequest = EncryptedRequest(encryptedData)

                val response = RetrofitClient.apiService(context).installConjunto(encryptedRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null && apiResponse.success) {
                        Log.d("installConjunto", "✅ Conjunto instalado com sucesso!")
                        return@withContext true
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                apiResponse?.message ?: "Erro ao instalar o sensor.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@withContext false
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Token inválido ou não autorizado."
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
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                return@withContext false
            }
        }
    }

    suspend fun criarUtilizador(nome: String, email: String, password: String, tipo: String): ApiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                // Criar o objeto de requisição
                val requestData = criarUtilizadorRequest(nome, email, password, tipo)

                // Criptografar os dados antes de enviar
                val encryptedData = encryptionUtils.encryptAES(Json.encodeToString(requestData))
                val encryptedRequest = EncryptedRequest(encryptedData)

                // Fazer a requisição à API
                val response = RetrofitClient.apiService(context).criarUtilizador(encryptedRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        val encryptedResponseData = apiResponse.data?.toString()

                        if (encryptedResponseData.isNullOrBlank()) {
                            Log.e(
                                "criarUtilizador",
                                "Os dados recebidos são inválidos para descriptografia!"
                            )
                            return@withContext null
                        }

                        // Descriptografar os dados recebidos
                        val decryptedData = encryptionUtils.decryptAES(encryptedResponseData)
                        return@withContext Json.decodeFromString<ApiResponse>(decryptedData)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                apiResponse?.message ?: "Erro ao criar o utilizador.",
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
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
                return@withContext null
            }
        }
    }

    suspend fun getAreas(): List<AreaData>? {
        return withContext(Dispatchers.IO) {
            try {
                // Realizando a requisição para obter as áreas
                val response = RetrofitClient.apiService(context).getAreas()

                // Verificando se a resposta foi bem-sucedida
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    // Verificando se a resposta da API está vazia ou nula
                    if (apiResponse.isNullOrEmpty()) {
                        Log.e("getAreas", "Nenhuma área encontrada")
                        return@withContext null
                    }

                    // Pegando os dados encriptados do primeiro item (supondo que seja uma lista)
                    val encryptedData = apiResponse[0].data?.toString()

                    // Verificando se os dados encriptados são válidos
                    if (encryptedData.isNullOrBlank()) {
                        Log.e("getAreas", "Os dados recebidos são inválidos para descriptografia!")
                        return@withContext null
                    }

                    // Descriptografando os dados recebidos
                    val decryptedData = encryptionUtils.decryptAES(encryptedData)

                    // Log dos dados descriptografados (para fins de depuração)
                    Log.d("getAreas", "🔹 Dados descriptografados: $decryptedData")

                    // Convertendo os dados descriptografados de JSON para Lista de AreaData
                    return@withContext Json.decodeFromString<List<AreaData>>(decryptedData)
                } else {
                    Log.e("getAreas", "Erro: ${response.errorBody()?.string()}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("getAreas", "Erro ao buscar áreas: ${e.localizedMessage}")
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

    object AreaManager {
        var AreaCriada: AreaData? = null
    }

}