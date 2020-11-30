package geekbrains.Kotlin.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import geekbrains.Kotlin.R
import geekbrains.Kotlin.data.notesRepository
import geekbrains.Kotlin.errors.NoAuthException

private const val RC_SIGN_IN = 458

class SplashActivity : AppCompatActivity() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SplashViewModel(notesRepository) as T
            }
        }).get(
                SplashViewModel::class.java
        )
    }

    private val layoutRes: Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutRes)

        viewModel.observeViewState().observe(this) {
            when (it) {
                is SplashViewState.Error -> renderError(it.error)
                SplashViewState.Auth -> renderData()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode != RC_SIGN_IN -> return
            resultCode != Activity.RESULT_OK -> finish()
            resultCode == Activity.RESULT_OK -> renderData()
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

    private fun renderData() {
        startMainActivity()
    }

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startloginActivity()
            else -> error.message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun startloginActivity() {
        val providers = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_launcher_foreground)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }
}