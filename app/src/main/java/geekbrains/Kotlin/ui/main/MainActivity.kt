package geekbrains.Kotlin.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import geekbrains.Kotlin.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }
}