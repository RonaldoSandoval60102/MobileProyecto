package com.example.proyectofinal.api

import com.example.proyectofinal.utils.DateTypeAdapter
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class RetrofitInstance {
    companion object {
        private const val BASE_URL = "https://notes-app-9c438.web.app/"

        fun getRetrofitInstance(): Retrofit {
            val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}