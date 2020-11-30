package geekbrains.Kotlin.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import geekbrains.Kotlin.R
import geekbrains.Kotlin.model.Note
import kotlinx.android.synthetic.main.note_fragment.*

@Suppress("UNCHECKED_CAST")
class NoteFragment : Fragment(R.layout.note_fragment) {
    private val note: Note? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getParcelable(NOTE_KEY)
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NoteViewModel(note) as T
            }
        }).get(
                NoteViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.note?.let {
            titleEt.setText(it.title)
            bodyEt.setText(it.note)
        }

        viewModel.showError().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Error while saving note!", Toast.LENGTH_LONG).show()
        }

        fab_confirm.setOnClickListener {
            viewModel.saveNote()
            activity?.onBackPressed()
        }

        titleEt.addTextChangedListener {
            viewModel.updateTitle(it?.toString() ?: "")
        }

        bodyEt.addTextChangedListener {
            viewModel.updateNote(it?.toString() ?: "")
        }
    }

    companion object {
        const val NOTE_KEY = "Note"

        fun create(note: Note? = null): NoteFragment {
            val fragment = NoteFragment()
            val arguments = Bundle()
            arguments.putParcelable(NOTE_KEY, note)
            fragment.arguments = arguments

            return fragment
        }
    }
}
