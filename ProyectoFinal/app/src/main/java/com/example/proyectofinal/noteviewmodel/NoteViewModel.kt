package com.example.proyectofinal.noteviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.example.proyectofinal.api.NoteId
import com.example.proyectofinal.models.Note
import com.example.proyectofinal.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class NoteViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _noteTitleText = MutableStateFlow("")
    val noteTitleText = _noteTitleText.asStateFlow()

    private val _noteBodyText = MutableStateFlow("")
    val noteBodyText = _noteBodyText.asStateFlow()

    var selectedNote: Note? = null

    private val _userNotes = MutableStateFlow<List<Note>>(emptyList())
    val userNotes: StateFlow<List<Note>> = _userNotes.asStateFlow()

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        getAllNotesByUser("usuario13")
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = noteRepository.login(username, password)
        }
    }

    fun getAllNotesByUser(userId: String) {
        viewModelScope.launch {
            noteRepository.getNotesByUser(userId)
                .catch { e ->
                    _error.value = e.message
                }
                .collect {
                    _userNotes.value = it
                }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            val result = noteRepository.insert(note)
            if (result) {
                val currentValue = _userNotes.value.toMutableList()
                currentValue.add(note)
                _userNotes.value = currentValue
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            val result = noteRepository.update(note)
            if (result) {
                val currentValue = _userNotes.value.toMutableList()
                val index = currentValue.indexOfFirst { it.id == note.id }
                if (index != -1) {
                    currentValue[index] = note
                    _userNotes.value = currentValue
                }
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            val title = noteTitleText.value
            val body = noteBodyText.value
            if (title.isNotBlank() && body.isNotBlank()) {
                if (selectedNote == null) {
                    val newNote = Note(
                        title = title,
                        body = body,
                        date = Date(),
                        latitude = 2.0,
                        longitude = 2.0,
                        userId = "usuario13",
                    )
                    insertNote(newNote)
                } else {
                    selectedNote?.let {
                        updateNote(
                            it.copy(
                                title = title,
                                body = body,
                            )
                        )
                    }
                }
                clearData()
            }
        }
    }

    fun deleteNoteById(id: String) {
        viewModelScope.launch {
            val result = noteRepository.deleteByIdNoteFromApi(id)
            if (result) {
                val currentNotes = _userNotes.value.toMutableList()
                currentNotes.removeAll { it.id == id }
                _userNotes.value = currentNotes
            }
        }
    }

    fun selectedNote(note: Note) {
        selectedNote = note
        _noteTitleText.value = note.title
        _noteBodyText.value = note.body
    }

    fun updateNoteTitleText(newTitle: String) {
        _noteTitleText.value = newTitle
    }

    fun updateNoteBodyText(newBody: String) {
        _noteBodyText.value = newBody
    }

    private fun clearData() {
        _noteTitleText.value = ""
        _noteBodyText.value = ""
        selectedNote = null
    }
}