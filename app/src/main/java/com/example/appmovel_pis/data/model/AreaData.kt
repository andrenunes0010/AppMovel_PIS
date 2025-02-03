package com.example.appmovel_pis.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaData(
    @SerialName("conjunto_id") val id: Int,
    val status: String,
    val tamanho: String? = null,  // Tornar opcional
    val email: String? = null,     // Tornar opcional
    val latitude: Double,
    val longitude: Double,
    @SerialName("area_nome") val nome: String
)
