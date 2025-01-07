package com.example.appmovel_pis.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
class EncryptionUtils() {

    fun desencriptarToken() {
        val token = "seu_token_jwt_aqui"
        val secretKey = "minha_chave_secreta"

        try {
            // Converter a chave secreta para bytes
            val keyBytes = secretKey.toByteArray()
            val key = Keys.hmacShaKeyFor(keyBytes)

            // Verificar e desencriptar o token
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body

            println("ID do Usuário: ${claims["userId"]}")
            println("Role: ${claims["role"]}")
            println("Token Expira em: ${claims.expiration}")
        } catch (e: Exception) {
            println("Token inválido ou erro ao verificar: ${e.message}")
        }
    }
}