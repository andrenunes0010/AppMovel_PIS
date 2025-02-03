package com.example.appmovel_pis.data.network

import com.example.appmovel_pis.data.model.ApiResponse
import com.example.appmovel_pis.data.model.AreaData
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    val tipo_area: String,
    val responsavel: String,
    var quantidades_conjuntos: Int,
    val latitude: String,
    val longitude: String
)

@Serializable
data class InstallConjuntoRequest(
    val area_id: Int,
    val latitude: String,
    val longitude: String
)

@Serializable
data class criarUtilizadorRequest(
    val nome: String,
    val email: String,
    val password: String,
    val tipo: String,
    val telemovel: String
)

@Serializable
data class AreasResponse(
    val conjuntosResults: List<AreaData>
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

    @POST("/api/v1/perfil/create")
    suspend fun criarUtilizador(@Body request: EncryptedRequest): Response<ApiResponse>

    @PUT("/api/v1/perfil/password")
    suspend fun mudarPassword(@Body request: EncryptedRequest): Response<ApiResponse>

    @GET("/api/v1/conjuntos/{userId}")
    suspend fun getAreas(@Path("userId") userId: Int): Response<ApiResponse>

}
