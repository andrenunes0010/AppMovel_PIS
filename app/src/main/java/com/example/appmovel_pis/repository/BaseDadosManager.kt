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

    suspend fun installArea(nome: String, tamanho: String, email: String, quantidadeConjuntos: Int, latitude: String, longitude: String): AreaData? {
        return withContext(Dispatchers.IO) {
            try {
                // Criar o objeto com os dados da requisição
                val requestData = InstallAreaRequest(
                    nome = nome,
                    tamanho_hectares = tamanho,
                    email = email,
                    Quantidades_conjuntos = quantidadeConjuntos,
                    latitude = latitude,
                    longitude = longitude
                )

                // Log dos dados antes da encriptação
                Log.d("installArea", "🔹 Dados originais da requisição: $requestData")

                // Encriptar os dados antes de enviar
                val encryptedData = encryptionUtils.encryptAES(Json.encodeToString(requestData))
                val encryptedRequest = EncryptedRequest(encryptedData)

                // Log dos dados encriptados antes de enviar
                Log.d("installArea", "🔹 Dados encriptados enviados: $encryptedData")

                // Enviar para o servidor
                val response = RetrofitClient.apiService(context).installArea(encryptedRequest)

                // Log do status da resposta HTTP
                Log.d("installArea", "🔹 Código de resposta HTTP: ${response.code()}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    // Log do corpo da resposta do servidor
                    Log.d("installArea", "🔹 Corpo da resposta: $apiResponse")

                    if (apiResponse != null && apiResponse.success) {
                        val encryptedResponseData =
                            apiResponse.data?.toString() // Garante que seja uma String

                        // Log dos dados encriptados recebidos
                        Log.d(
                            "installArea",
                            "🔹 Dados encriptados recebidos: $encryptedResponseData"
                        )

                        // Verificar se os dados recebidos são válidos
                        if (encryptedResponseData.isNullOrBlank()) {
                            Log.e("installArea", "⚠️ Erro: Dados encriptados vazios ou inválidos!")
                            return@withContext null
                        }

                        // Descriptografar os dados recebidos
                        val decryptedData = encryptionUtils.decryptAES(encryptedResponseData)

                        // Log dos dados após descriptografar
                        Log.d("installArea", "🔹 Dados descriptografados: $decryptedData")

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
                        401 -> "❌ Erro 401: Token inválido ou não autorizado."
                        else -> response.errorBody()?.string() ?: "Erro desconhecido do servidor."
                    }

                    // Log do erro do servidor
                    Log.e("installArea", "⚠️ Erro na resposta: $errorMessage")

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    return@withContext null
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // Log do erro inesperado
                Log.e("installArea", "🚨 Erro inesperado: ${e.localizedMessage}")

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
                return@withContext null
            }
        }
    }

    suspend fun installConjunto(Latitude: String, Longitude: String, DataInstalacao: String, Status: String): SensorData? {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = InstallConjuntoRequest(
                    Latitude = Latitude,
                    Longitude = Longitude,
                    DataInstalacao = DataInstalacao,
                    Status = Status
                )

                val encryptedData = encryptionUtils.encryptAES(Json.encodeToString(requestData))
                val encryptedRequest = EncryptedRequest(encryptedData)

                val response = RetrofitClient.apiService(context).installConjunto(encryptedRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        val encryptedSensorData =
                            apiResponse.data?.toString() // Garante que seja uma String

                        Log.d(
                            "installConjunto",
                            "Dados encriptados recebidos: $encryptedSensorData"
                        )

                        // Verifica se os dados não são nulos ou vazios
                        if (encryptedSensorData.isNullOrBlank()) {
                            Log.e(
                                "installConjunto",
                                "Os dados recebidos são inválidos para descriptografia!"
                            )
                            return@withContext null
                        }

                        // Descriptografar os dados e convertê-los para SensorData
                        val decryptedData = encryptionUtils.decryptAES(encryptedSensorData)
                        return@withContext Json.decodeFromString<SensorData>(decryptedData)
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
                    Toast.makeText(context, "Erro: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
                null
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
}