package geekbrains.Kotlin.ui.main

import geekbrains.Kotlin.data.Note

sealed class ViewState {
    data class Value(val notes: List<Note>) : ViewState()
    object EMPTY : ViewState()
}