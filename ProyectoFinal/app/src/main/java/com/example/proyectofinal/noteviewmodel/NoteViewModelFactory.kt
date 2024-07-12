package com.example.proyectofinal.noteviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.api.ApiService
import com.example.proyectofinal.api.RetrofitInstance
import com.example.proyectofinal.models.UserPreferences
import com.example.proyectofinal.repository.NoteRepository
import com.example.proyectofinal.room.NoteAppDataBase

class NoteViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            val database = NoteAppDataBase.getDatabase(context)
            val noteDao = database.noteDao
            val noteApiService = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            val noteRepository = NoteRepository(noteDao, noteApiService)
            return NoteViewModel(noteRepository) as T
        }
        return super.create(modelClass)
    }
}