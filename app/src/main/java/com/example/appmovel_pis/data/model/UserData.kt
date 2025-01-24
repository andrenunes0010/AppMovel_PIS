package com.example.appmovel_pis.data.model

import kotlinx.serialization.Serializable

// Modelo para os dados específicos do usuário
@Serializable
data class UserData(
    val id: Int,
    val nome: String?,
    val email: String?,
    val token: String?,
    val tipo: String?
)