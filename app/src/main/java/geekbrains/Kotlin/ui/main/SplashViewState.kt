package geekbrains.Kotlin.ui.main

sealed class SplashViewState {
    class Error(val error: Throwable) : SplashViewState()
    object Auth : SplashViewState()
}