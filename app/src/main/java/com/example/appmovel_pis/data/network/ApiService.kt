package com.example.appmovel_pis.data.network

import com.example.appmovel_pis.data.model.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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
    val password: String,
    val password_now: String
)

@Serializable
data class InstallAreaRequest(
    val nome: String,
    val tamanho_hectares: String,
    val email: String,
    var Quantidades_conjuntos: Int,
    val latitude: String,
    val longitude: String
)

@Serializable
data class InstallConjuntoRequest(
    val Latitude: String,
    val Longitude: String,
    val DataInstalacao: String,
    val Status: String
)

@Serializable
data class criarUtilizadorRequest(
    val nome: String,
    val email: String,
    val password: String,
    val tipo: String
)

data class EncryptedRequest(
    val data: String
)

interface ApiService {
    @POST("/api/v1/login")
    suspend fun login(@Body request: EncryptedRequest): Response<ApiResponse>

    @POST("/api/v1/areas")
    suspend fun installArea(@Body request: EncryptedRequest): Response<ApiResponse>

    @POST("/api/v1/conjuntos")
    suspend fun installConjunto(@Body request: EncryptedRequest): Response<ApiResponse>

    @POST("/api/v1/users")
    suspend fun criarUtilizador(@Body request: EncryptedRequest): Response<ApiResponse>

    @PUT("/api/v1/perfil/password")
    suspend fun mudarPassword(@Body request: EncryptedRequest): Response<ApiResponse>

    @GET("/api/v1/conjuntos")
    suspend fun getAreas(): Response<List<ApiResponse>>
}