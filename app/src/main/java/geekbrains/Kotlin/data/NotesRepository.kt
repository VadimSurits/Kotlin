package geekbrains.Kotlin.data

import androidx.lifecycle.LiveData
import geekbrains.Kotlin.model.Note
import geekbrains.Kotlin.model.User

interface NotesRepository {
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
    fun getCurrentUser(): User?
}