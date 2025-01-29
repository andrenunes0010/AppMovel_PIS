package com.example.appmovel_pis.data.network

import android.content.Context
import com.example.appmovel_pis.data.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Interceptor para adicionar o token ao cabeçalho Authorization
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionManager = SessionManager(context)
        val token = sessionManager.getUser()?.token

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("authorization", "$token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://api.sylvanus.website"

    fun apiService(context: Context): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Adiciona o interceptor
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
