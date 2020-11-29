package geekbrains.Kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import geekbrains.Kotlin.data.Repository

class MainViewModel : ViewModel() {
    fun observeViewState(): LiveData<ViewState> = Repository.observeNotes().map {
        if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
    }
}