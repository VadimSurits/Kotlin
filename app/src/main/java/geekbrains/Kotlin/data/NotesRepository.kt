package geekbrains.Kotlin.data

import androidx.lifecycle.LiveData

interface NotesRepository {
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
}