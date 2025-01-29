package com.example.appmovel_pis.data.model

// Modelo para a resposta genérica do servidor
data class ApiResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: String,  // Aqui é uma string criptografada
    val datetime: String
)