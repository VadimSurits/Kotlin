package geekbrains.Kotlin.ui.main

import androidx.lifecycle.ViewModel
import geekbrains.Kotlin.data.Note
import geekbrains.Kotlin.data.Repository

class NoteViewModel(var note: Note?) : ViewModel() {

    fun updateNote(text: String) {
        note = (note ?: generateNote()).copy(note = text)
    }

    fun updateTitle(text: String) {
        note = (note ?: generateNote()).copy(title = text)
    }

    override fun onCleared() {
        super.onCleared()

        note?.let {
            Repository.addOrReplaceNote(it)
        }
    }

    private fun generateNote(): Note {
        return Note()
    }
}