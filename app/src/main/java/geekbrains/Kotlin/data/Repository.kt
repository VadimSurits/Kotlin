package geekbrains.Kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

private val idRandom = Random(0)
val noteId: Long
    get() = idRandom.nextLong()


object Repository {
    val notes: MutableList<Note> = mutableListOf()
    private val allNotes = MutableLiveData(getListForNotify())

    fun observeNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun addOrReplaceNote(newNote: Note) {
        notes.find { it.id == newNote.id }?.let {
            if (it == newNote) return

            notes.remove(it)
        }

        notes.add(newNote)

        allNotes.postValue(
                getListForNotify()
        )
    }

    private fun getListForNotify(): List<Note> = notes.toMutableList().also {
        it.reverse()
    }
}