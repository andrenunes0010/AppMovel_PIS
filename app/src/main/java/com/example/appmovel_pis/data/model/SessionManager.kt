package com.example.appmovel_pis.data

import android.content.Context
import com.example.appmovel_pis.data.model.UserData

class SessionManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)

    // Salva os dados do usuário no SharedPreferences
    fun saveUser(user: UserData) {
        sharedPreferences.edit()
            .putInt("id", user.id)
            .putString("nome", user.nome)
            .putString("email", user.email)
            .putString("token", user.token)
            .putString("tipo", user.tipo)
            .apply()
    }

    // Recupera os dados do usuário armazenados no SharedPreferences
    fun getUser(): UserData? {
        val id = sharedPreferences.getInt("id", -1)
        val nome = sharedPreferences.getString("nome", null)
        val email = sharedPreferences.getString("email", null)
        val token = sharedPreferences.getString("token", null)
        val tipo = sharedPreferences.getString("tipo", null)

        return if (id != -1 && nome != null && email != null && token != null && tipo != null) {
            UserData(id, nome, email, token, tipo)
        } else {
            null
        }
    }

    // Limpa os dados da sessão
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}
