package com.example.appmovel_pis.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT

class EncryptionUtils(private val secretKey: String) {

    // Valida o token JWT
    fun validateToken(token: String): Boolean {
        return try {
            val algorithm = Algorithm.HMAC256(secretKey)
            val verifier = JWT.require(algorithm).build()
            verifier.verify(token) // Verifica a assinatura e a expiração
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Extrai o payload do token JWT
    fun extractPayload(token: String): Map<String, String?> {
        return try {
            val algorithm = Algorithm.HMAC256(secretKey)
            val jwt: DecodedJWT = JWT.require(algorithm).build().verify(token)

            // Cria um mapa com os dados do payload
            jwt.claims.mapValues { it.value.asString() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap() // Retorna um mapa vazio em caso de erro
        }
    }
}
