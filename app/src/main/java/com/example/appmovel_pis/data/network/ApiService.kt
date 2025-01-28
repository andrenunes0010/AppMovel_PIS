package com.example.appmovel_pis.data.network

import com.example.appmovel_pis.data.model.ApiResponse
import com.example.appmovel_pis.data.model.SensorData
import com.example.appmovel_pis.data.model.UserData
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

// Modelo para a requisição de login
@Serializable
data class LoginRequest(
    val email: String,
    val senha: String
)

@Serializable
data class mudarPassword(
    val token: String,
    val palavraPasseAntiga: String,
    val palavraPasseNova: String
)

data class InstallRequest(
    val email: String,
    val Latitude: String,
    val Longitude: String,
    val DataInstalacao: String,
    val Status: String,
    val NomeSensor: String,
    val token: String
)

data class EncryptedRequest(
    val data: String
)

interface ApiService {
    @POST("/api/v1/login")
    suspend fun login(@Body request: EncryptedRequest): Response<ApiResponse>

    @PUT("/api/v1/perfil/password")
    suspend fun mudarPassword(@Body request: EncryptedRequest): Response<ApiResponse>

    //@POST("/api/v1/install")
    //suspend fun install(@Body request: InstallRequest): Response<ApiResponse<SensorData>>
}
