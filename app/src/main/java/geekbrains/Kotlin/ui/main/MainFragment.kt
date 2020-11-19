package geekbrains.Kotlin.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import geekbrains.Kotlin.R
import geekbrains.Kotlin.ui.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(R.layout.main_fragment) {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = MainAdapter()
        mainRecycler.adapter = adapter

        viewModel.viewState().observe(viewLifecycleOwner, { t ->
            t?.let { adapter.notes = it.notes }
        })
    }

}