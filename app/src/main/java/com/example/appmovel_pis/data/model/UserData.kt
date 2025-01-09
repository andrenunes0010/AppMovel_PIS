package com.example.appmovel_pis.data.model

// Modelo para os dados específicos do usuário
data class UserData(
    val id: Int,
    val nome: String,
    val email: String,
    val token: String,
    val tipo: String
)