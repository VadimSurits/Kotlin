package geekbrains.Kotlin.ui.main.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import geekbrains.Kotlin.data.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(note: Note) {
        with(itemView) {
            title.text = note.title
            body.text = note.note
            setBackgroundColor(note.color)
        }
    }
}