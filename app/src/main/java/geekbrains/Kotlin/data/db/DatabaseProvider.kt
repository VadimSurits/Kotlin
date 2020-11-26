package geekbrains.Kotlin.data.db

import androidx.lifecycle.LiveData
import geekbrains.Kotlin.data.Note

//Теперь создадим классы доступа к данным. В пакете data приложения добавим пакет db. Чтобы
//отвязать реализацию логики хранения данных от остальной логики приложения, создадим в пакете
//db интерфейс DatabaseProvider :
interface DatabaseProvider {
    //Так как запросы к базе данных асинхронные, нужен механизм отложенного получения результата.
    //Будем использовать для этого LiveData.
    fun observeNotes(): LiveData<List<Note>>
    fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>>
}