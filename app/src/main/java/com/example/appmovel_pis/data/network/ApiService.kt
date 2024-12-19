package com.example.appmovel_pis.data.network

import com.example.appmovel_pis.data.model.ApiResponse
import com.example.appmovel_pis.data.model.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Modelo para a requisição de login
data class LoginRequest(val email: String, val senha: String)

// Modelo para a resposta do login
data class LoginResponse(val id: Int, val nome: String, val email: String)
data class LoginErrorResponse(val message: String)

interface ApiService {
    @POST("/api/v1/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserData>>
}
