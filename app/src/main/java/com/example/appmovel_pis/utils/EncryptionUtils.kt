package com.example.appmovel_pis.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier

class EncryptionUtils(private val secret: String) {

    // Decodifica o token JWT sem validar a assinatura
    fun decodeToken(token: String): DecodedJWT {
        return JWT.decode(token)
    }

    // Valida a assinatura do token JWT
    fun validateToken(token: String): DecodedJWT? {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            val verifier: JWTVerifier = JWT.require(algorithm).build()
            verifier.verify(token) // Lança uma exceção se o token for inválido
        } catch (e: Exception) {
            e.printStackTrace()
            null // Retorna null se o token for inválido ou a assinatura estiver incorreta
        }
    }

    // Extrai dados do payload do token
    fun extractPayload(token: String): Map<String, String> {
        val decodedJWT = decodeToken(token)
        val claims = mutableMapOf<String, String>()

        decodedJWT.claims.forEach { (key, value) ->
            claims[key] = value.asString()
        }

        return claims
    }
}
