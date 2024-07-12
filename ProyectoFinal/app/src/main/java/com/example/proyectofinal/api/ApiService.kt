package com.example.proyectofinal.api


import com.example.proyectofinal.models.Note
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

interface ApiService {
    @GET("notes")
    suspend fun getNotesByUser(
        @Query("user_id") userId: String
    ): List<Note>

    @HTTP(method = "POST", path = "notes", hasBody = true)
    suspend fun createNote(
        @Body note: NoteRequest
    ): String

    @HTTP(method = "PUT", path = "notes", hasBody = true)
    suspend fun updateNote(
        @Body note: NoteUpdate
    ): String

    @HTTP(method = "DELETE", path = "notes", hasBody = true)
    suspend fun deleteNote(@Body noteId: NoteId): Response<Unit>

    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body loginRequest: LoginRequest): NoteId

}

data class NoteId(val id: String)

data class NoteRequest(
    val titulo: String,
    val latitud: Double,
    val longitud: Double,
    val user_id: String,
    val fecha: Date,
    val body: String
)

data class NoteUpdate(
    val id: String,
    val titulo: String,
    val latitud: Double,
    val longitud: Double,
    val user_id: String,
    val fecha: Date,
    val body: String
)

data class LoginRequest(
    val username: String,
    val password: String
)