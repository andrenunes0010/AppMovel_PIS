package com.example.appmovel_pis.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AreaData(
    val id: Int,
    val nome: String,
    val tamanho: String,
    val email: String,
    val status: String,
    val latitude: Double,
    val longitude: Double
)
