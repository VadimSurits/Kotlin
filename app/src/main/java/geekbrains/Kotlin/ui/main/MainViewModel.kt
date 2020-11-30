package geekbrains.Kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import geekbrains.Kotlin.data.notesRepository

//Класс MainViewModel наследуется от AndroidViewModel из библиотеки Architecture components.

//LiveData — это класс из библиотеки architecture components. Будем использовать его, чтобы
//передавать данные во view, которой в данном случае будет MainFragment. Получаем из репозитория
// список заметок.

class MainViewModel : ViewModel() {
    fun observeViewState(): LiveData<ViewState> = notesRepository.observeNotes()
            .map {
                if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
            }
}