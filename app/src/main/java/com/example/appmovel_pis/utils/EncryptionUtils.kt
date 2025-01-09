package com.example.appmovel_pis.utils

import android.util.Base64
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionUtils(private val secretKey: String) {

    // AES - Configurações
    private val aesCipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private val aesIv = ByteArray(16) // Vetor de inicialização (IV) fixo para exemplo (não recomendado para produção)

    private val secretKeyAES = "1234567890abcdef".toByteArray()
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

    // AES - Criptografar mensagem
    fun encryptAES(message: String): String {
        return try {
            val secretKeySpec = SecretKeySpec(secretKeyAES, "AES")
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(aesIv))
            val encryptedBytes = aesCipher.doFinal(message.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    // AES - Descriptografar mensagem
    fun decryptAES(encryptedMessage: String): String {
        return try {
            val secretKeySpec = SecretKeySpec(secretKeyAES, "AES")
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(aesIv))
            val decodedBytes = Base64.decode(encryptedMessage, Base64.DEFAULT)
            String(aesCipher.doFinal(decodedBytes), Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
