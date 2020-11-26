package geekbrains.Kotlin.data

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import geekbrains.Kotlin.R
import kotlinx.android.parcel.Parcelize

//Класс Note: Главная часть приложения — это заметка. У нее несколько свойств: id, заголовок,
// сама заметка и цвет ее фона.

//Метод doc.toObject(Note::class.java) в методе subscribeForDbChanging() в классе FireStoreProvider
//занимается тем, что трансформирует данные (маппинг), полученные с сервера FireBase , в данные типа
//Note. Это значит, что в классе Note должен быть пустой конструктор — это обязательный контракт для
//маппинга данных через FireBase. Поэтому нам нужно немного доработать класс Note. Добавим каждому
//аргументу в конструкторе класса значения по умолчанию. Таким образм если из облака ничего не
//придет или все параметры будут пустые, то у класса Note будет вызван пустой конструктор(полей нет),
//а поля класса Note засеттятся по умолчанию.
@Parcelize
data class Note(
        val id: Long = noteId,
        val title: String = "",
        val note: String = "",
        val color: Color = Color.values()[(Color.values().indices).random()]
) : Parcelable

enum class Color {
    WHITE,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK
}

fun Color.mapToColor(context: Context): Int {
    val id = when (this) {
        Color.WHITE -> R.color.color_white
        Color.YELLOW -> R.color.color_yellow
        Color.GREEN -> R.color.color_green
        Color.BLUE -> R.color.color_blue
        Color.RED -> R.color.color_red
        Color.VIOLET -> R.color.color_violet
        Color.PINK -> R.color.color_pink
    }
    return ContextCompat.getColor(context, id)
}