package com.example.proyectofinal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.proyectofinal.api.ApiService
import com.example.proyectofinal.api.LoginRequest
import com.example.proyectofinal.api.NoteId
import com.example.proyectofinal.api.NoteRequest
import com.example.proyectofinal.api.NoteUpdate
import com.example.proyectofinal.room.NoteDao
import com.example.proyectofinal.models.Note
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import retrofit2.awaitResponse

class NoteRepository(
    private val noteDao: NoteDao,
    private val apiService: ApiService
) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()


    fun getNotesByUser(userId: String) = flow {
        try {
            val notes = apiService.getNotesByUser(userId)
            println(notes)
            emit(notes)
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error getting notes", e)
            emit(emptyList<Note>())
        }
    }

    suspend fun deleteByIdNoteFromApi(id: String): Boolean {
        return try {
            println("Attempting to delete note with ID: $id")
            val response = apiService.deleteNote(NoteId(id))
            println("Response: $response")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error deleting note", e)
            false
        }
    }

    suspend fun insert(note: Note): Boolean {
        return try {

            val noteRequest = NoteRequest(
                note.title,
                note.latitude,
                note.longitude,
                note.userId,
                note.date,
                note.body
            )

            val response = apiService.createNote(noteRequest)
            println("Response: $response")
            response.isNotEmpty()
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error inserting note", e)
            false
        }
    }

    suspend fun update(note: Note) : Boolean {
        return try {
            val noteRequest = NoteUpdate(
                note.id,
                note.title,
                note.latitude,
                note.longitude,
                note.userId,
                note.date,
                note.body
            )

            val response = apiService.updateNote(noteRequest)
            println("Response: $response")
            response.isNotEmpty()
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error updating note", e)
            false
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            Result.success(response.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}