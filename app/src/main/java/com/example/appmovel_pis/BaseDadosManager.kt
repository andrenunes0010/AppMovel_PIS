package com.example.appmovel_pis

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class BaseDadosManager  {
    private val url = "jdbc:mysql://promac.servehttp.com:1111/ips"
    private val user = "guga"
    private val password = "RuNO9pCT"

    private fun getConexao(): Connection {
        return DriverManager.getConnection(url, user, password)
    }

    fun getUtilizador(email: String, senha: String): Utilizador? {
        val utilizadores = mutableListOf<Utilizador>()
        val query = "SELECT * FROM utilizador WHERE email = ? AND password = ?"
        val conexao = getConexao()

        try {
            val statement = conexao.createStatement()
            val resultSet: ResultSet = statement.executeQuery(query)

            if (resultSet != null) {
                val idUtilizador = resultSet.getInt("id")
                val nomeUtilizador = resultSet.getString("nome")
                val emailUtilizador = resultSet.getString("email")

                val utilizador = Utilizador(idUtilizador, nomeUtilizador, emailUtilizador)
                utilizadores.add(utilizador)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            conexao.close()
        }
        return null
    }
}

//Classe Utilizador
data class Utilizador(val id: Int, val nome: String, val email: String)