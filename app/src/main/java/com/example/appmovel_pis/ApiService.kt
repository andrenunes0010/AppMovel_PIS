package com.example.appmovel_pis

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Modelo para a requisição de login
data class LoginRequest(val email: String, val senha: String)

// Modelo para a resposta do login
data class LoginResponse(val id: Int, val nome: String, val email: String)

interface ApiService {
    @POST("AppLogin")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
