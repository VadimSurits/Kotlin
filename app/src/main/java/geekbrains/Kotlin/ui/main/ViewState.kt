package geekbrains.Kotlin.ui.main

import geekbrains.Kotlin.data.Note

//Класс ViewState представляет состояние view и служит для передачи данных из ViewModel во view.

sealed class ViewState {
    data class Value(val notes: List<Note>) : ViewState()
    object EMPTY : ViewState()
}