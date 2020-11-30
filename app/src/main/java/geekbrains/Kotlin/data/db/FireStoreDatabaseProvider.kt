package geekbrains.Kotlin.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import geekbrains.Kotlin.errors.NoAuthException
import geekbrains.Kotlin.model.Note
import geekbrains.Kotlin.model.User

//Корневым элементом структуры данных будет коллекция notes. Вынесем это имя в константу
//верхнего уровня.
private const val NOTES_COLLECTION = "notes"

//Константа для users
private const val USERS_COLLECTION = "users"

//Также создали свойство TAG для отображения в логах класса, из которого эти логи.
const val TAG = "FireStoreDatabase"

//Реализуем интерфейс DatabaseProvider в классе FireStoreDatabaseProvider. Но для начала поясним,
//как хранятся данные в Firestore. Корневым элементом структуры данных является коллекция,
//которая содержит документы — наборы данных типа «ключ-значение». Ключ всегда типа String , а
//значение может быть строковым, числовым, булевым, массивом, словарем («ключ-значение») и т.д.
//Документ может хранить бинарные данные (например, изображение), но делать это не рекомендуется.
//Также документ может содержать вложенные коллекции. Размер документа без учета вложенных коллекций
//— до 1 Мб. Уровень вложенности коллекций и документов — до 100 уровней. Нет ограничения на
//хранение документов разного типа в одной коллекции, но делать так — плохая идея: высока
//вероятность ошибок интерпретации типа таких документов при получении.

class FireStoreDatabaseProvider : DatabaseProvider {
    //Чтобы создать новую коллекцию, достаточно получить экземпляр базы данных для приложения:
    private val db = FirebaseFirestore.getInstance()

    private val result = MutableLiveData<List<Note>>()

    // Получение currentUser из FirebaseAuth
    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    //Есть ссылка на коллекцию — создаем методы для получения и сохранения документов в ней:
    private var subscribedOnDb = false

    override fun observeNotes(): LiveData<List<Note>> {
        if (!subscribedOnDb) subscribeForDbChanging()
        return result
    }

    //Метод для получения нового user-а
    override fun getCurrentUser() = currentUser?.run { User(displayName,email) }

    //Чтобы получить ссылку на документ, надо вызвать метод document(), передав в него имя документа.
    //Это будет id заметки, так как оно уникально и хранится в заметке. FireStore сама может генерировать
    //уникальные имена документов, но в этом случае придется их сохранять, что в данной ситуации
    //излишне.
    //Чтобы сохранить новый документ или заменить данные в существующем, есть метод set() , в который
    //можем передать экземпляр заметки. Метод set() возвращает объект типа Task — задание для базы
    //данных на проведение транзакции.
    //Чтобы получить callback при успешном сохранении документа, вызовем у него метод
    //addOnSuccessListener. Для callback’а при неудачном сохранении документа добавим
    // addOnFailureListener.
    override fun addOrReplaceNote(newNote: Note): LiveData<Result<Note>> {
        val result = MutableLiveData<Result<Note>>()

        handleNotesReference(
                {
                    getUserNotesCollection()
                            .document(newNote.id.toString())
                            .set(newNote)
                            .addOnSuccessListener {
                                Log.d(TAG, "Note $newNote is saved")
                                result.value = Result.success(newNote)
                            }
                            .addOnFailureListener {
                                Log.e(TAG, "Error saving note $newNote, message: ${it.message}")
                                result.value = Result.failure(it)
                            }
                }, {
            Log.e(TAG, "Error getting reference note $newNote, message: ${it.message}")
            result.value = Result.failure(it)
        }
        )
        return result
    }

    //Метод получения заметок конкретного пользователя
    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    //При изменении или добавлении документа сразу получим обновленную коллекцию.
    private fun subscribeForDbChanging() {
        handleNotesReference(
                {
                    getUserNotesCollection().addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e(TAG, "Observe note exception:$e")
                        } else if (snapshot != null) {
                            val notes = mutableListOf<Note>()

                            for (doc: QueryDocumentSnapshot in snapshot) {
                                notes.add(doc.toObject(Note::class.java))
                            }
                            result.value = notes
                        }
                    }
                    subscribedOnDb = true
                }, {
            Log.e(TAG, "Error getting reference while subscribed for notes")
        })
    }
    //Метод doc.toObject(Note::class.java) занимается тем, что трансформирует данные (маппинг),
    //полученные с сервера FireBase, в данные типа Note. Это значит, что в классе Note должен быть
    //пустой конструктор — это обязательный контракт для маппинга данных через FireBase. Поэтому
    //необходимо доработать класс Note.

    private inline fun handleNotesReference(
            referenceHandler: (CollectionReference) -> Unit,
            exceptionHandler: (Throwable) -> Unit = {}
    ) {
        kotlin.runCatching {
            getUserNotesCollection()
        }
                .fold(referenceHandler, exceptionHandler)
    }
}
