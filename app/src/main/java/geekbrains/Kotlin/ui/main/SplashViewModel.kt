package geekbrains.Kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.Kotlin.data.NotesRepository
import geekbrains.Kotlin.errors.NoAuthException
import java.util.concurrent.Executors

class SplashViewModel(private val repository: NotesRepository) : ViewModel() {
    private val viewStateLiveData = MutableLiveData<SplashViewState>()

    init {
        Executors.newSingleThreadExecutor()
                .submit {
                    requestUser()
                }
    }

    fun observeViewState(): LiveData<SplashViewState> = viewStateLiveData

    private fun requestUser() {
        val user = repository.getCurrentUser()

        viewStateLiveData.postValue(
                if (user != null) {
                    SplashViewState.Auth
                } else {
                    SplashViewState.Error(error = NoAuthException())
                }
        )
    }
}