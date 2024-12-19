package com.example.appmovel_pis.data.model

// Modelo para a resposta genérica do servidor
data class ApiResponse<T>(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: T?, // Dados retornados podem ser nulos
    val datetime: String
)

