package com.example.appmovel_pis.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorData (
    val email: String,
    val Latitude: String,
    val Longitude: String,
    val DataInstalacao: String,
    val Status: String,
    val NomeSensor: String
)