package com.example.appmovel_pis.data.network

import com.example.appmovel_pis.data.model.ApiResponse
import com.example.appmovel_pis.data.model.SensorData
import com.example.appmovel_pis.data.model.UserData
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Modelo para a requisição de login
@Serializable
data class LoginRequest(
    val email: String,
    val senha: String
)
data class InstallRequest(val email: String, val Latitude: String, val Longitude: String, val DataInstalacao: String, val Status: String, val NomeSensor: String, val token: String)
data class EncryptedRequest(val data: String)

interface ApiService {
    @POST("/api/v1/login")
    suspend fun login(@Body request: EncryptedRequest): Response<ApiResponse>
    //suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserData>>

    //@POST("/api/v1/install")
    //suspend fun install(@Body request: InstallRequest): Response<ApiResponse<SensorData>>
}
