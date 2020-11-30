package geekbrains.Kotlin.data

import androidx.lifecycle.LiveData
import geekbrains.Kotlin.data.db.FireStoreDatabaseProvider
import geekbrains.Kotlin.model.Note
import java.util.*

//Repository - это класс, который отвечает за получение и сохранение заметок.

private val idRandom = Random(0)
val noteId: Long
    get() = idRandom.nextLong()

class Repository(private val provider: FireStoreDatabaseProvider) : NotesRepository {

    override fun getCurrentUser() = provider.getCurrentUser()

    override fun observeNotes(): LiveData<List<Note>> {
        return provider.observeNotes()
    }

    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        return provider.addOrReplaceNote(newNote)
    }
}

val notesRepository: NotesRepository by lazy { Repository(FireStoreDatabaseProvider()) }
