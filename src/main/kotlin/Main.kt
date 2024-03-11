import java.lang.RuntimeException
import javax.annotation.processing.Messager

class Comments { // Информация о комментариях к записи, объект с полями
    private var count: Int = 0 //количество комментариев
    private var canPost: Boolean =
        true //информация о том, может ли текущий пользователь комментировать запись (1 — может, 0 — не может);
    private var groupsCanPost: Boolean = true //информация о том, могут ли сообщества комментировать запись
    private var canClose: Boolean = true //может ли текущий пользователь закрыть комментарии к записи
    private var canOpen: Boolean = true //может ли текущий пользователь открыть комментарии к записи

    fun setCount() {
        this.count += 1
    }

    fun getCount(): Int {
        return count
    }
}

/*
Комментарий на стене
Объект, описывающий комментарии на стене, содержит следующие поля:
id integer Идентификатор комментария.
from_id integer Идентификатор автора комментария.
date integer Дата создания комментария в формате Unixtime.
text string Текст комментария.
donut object Информация о VK Donut. Объект со следующими полями:
• is_don (boolean) — является ли комментатор подписчиком VK Donut.
• placeholder (string) — заглушка для пользователей, которые не оформили подписку VK Donut.
reply_to_user integer Идентификатор пользователя или сообщества, в ответ которому оставлен текущий комментарий (если применимо).
reply_to_comment integer Идентификатор комментария, в ответ на который оставлен текущий (если применимо).
attachments object Медиавложения комментария (фотографии, ссылки и т.п.). Описание массива attachments находится на отдельной странице.
parents_stack array Массив идентификаторов родительских комментариев.
thread object Информация о вложенной ветке комментариев, объект с полями:
•count (integer) — количество комментариев в ветке.
•items (array) — массив объектов комментариев к записи (только для метода wall.getComments).
•can_post (boolean) – может ли текущий пользователь оставлять комментарии в этой ветке.
•show_reply_button (boolean) – нужно ли отображать кнопку «ответить» в ветке.
•groups_can_post (boolean) – могут ли сообщества оставлять комментарии в ветке.
 */

class Donut {
    private var isDon: Boolean = false // (boolean) — является ли комментатор подписчиком VK Donut.
    private var placeholder: String =
        "0" //(string) — заглушка для пользователей, которые не оформили подписку VK Donut.
}

class ArrayParentsStackComment { //Массив идентификаторов родительских комментариев
    private var commentsParentsStack = HashSet<Long>()
    fun addId(id: Long): Long {
        commentsParentsStack.add(id)
        return commentsParentsStack.last()
    }
}

class ItemsComments { //массив объектов комментариев к записи (только для метода wall.getComments)
    private var comments = emptyArray<Comment>()
    private var countId: Long = 0
    fun add(comment: Comment): Comment {
        comments += comment.copy(id = ++countId)
        return comments.last()
    }
}

class Thread { //Информация о вложенной ветке комментариев, объект с полями
    private var count: Long = 0 // (integer) — количество комментариев в ветке.

    //private var items: ItemsComments? = null // (array) — массив объектов комментариев к записи (только для метода wall.getComments).
    private var canPost: Boolean = true // (boolean) – может ли текущий пользователь оставлять комментарии в этой ветке.
    private var showReplyButton: Boolean = true // (boolean) – нужно ли отображать кнопку «ответить» в ветке.
    private var groupsCanPost: Boolean = true // (boolean) – могут ли сообщества оставлять комментарии в ветке.
}

data class Comment(
    var id: Long, // integer Идентификатор комментария.
    var fromId: Long, // integer Идентификатор автора комментария.
    var date: Long = System.currentTimeMillis(), // integer Дата создания комментария в формате Unixtime.
    var text: String = "", // string Текст комментария.
    var donut: Donut,
    var replyToUser: Long, // integer Идентификатор пользователя или сообщества, в ответ которому оставлен текущий комментарий (если применимо).
    var replyToComment: Long, // integer Идентификатор комментария, в ответ на который оставлен текущий (если применимо).
    var arrayAttachments: ArrayAttachments?, // object Медиавложения комментария (фотографии, ссылки и т.п.). Описание массива attachments находится на отдельной странице.
    var parents_stack: ArrayParentsStackComment?, // array Массив идентификаторов родительских комментариев.
    var thread: Thread? //Информация о вложенной ветке комментариев, объект с полями
)

class Likes { //Информация о лайках к записи, объект с полями
    private var count: Int = 0 // число пользователей, которым понравилась запись
    private var userLikes: Boolean = true// наличие отметки «Мне нравится» от текущего пользователя (1 — есть, 0 — нет)
    private var canLike: Boolean =
        true // информация о том, может ли текущий пользователь поставить отметку «Мне нравится» (1 — может, 0 — не может)
    private var canPublish: Boolean =
        true // информация о том, может ли текущий пользователь сделать репост записи (1 — может, 0 — не может)

    fun setCount() {
        this.count += 1
    }

    fun getCount(): Int {
        return count
    }
}

interface Attachment {
    val type: String
}

class ArrayAttachments() {
    private var attachments = emptyArray<Attachment>()
    fun addAttachment(attachment: Attachment): Attachment {
        attachments += attachment
        return attachments.last()
    }
}

data class PhotoAttachment(
    var photo: Photo
) : Attachment {
    //Можно перенести описание свойства type в наследниках в тело класса,
    // чтобы не надо было его задавать при создании экземпляров классов
    override val type: String = "photo"
}

data class Photo(
    var id: Int,
    var owner_id: Int,
    var link: String,
    var anotherLink: String
)

data class ImageAttachment(
    var image: Image
) : Attachment {
    override val type: String = "image"
}

data class Image(
    var id: Int,
    var owner_id: Int,
    var link: String,
    var height: Int,
    var width: Int
)

data class VideoAttachment(
    var video: Video
) : Attachment {
    override val type: String = "video"
}

data class Video(
    var id: Int,
    var owner_id: Int,
    var title: String,
    var duration: Int
)

data class AudioAttachment(
    var audio: Audio
) : Attachment {
    override val type: String = "audio"
}

data class Audio(
    var id: Int,
    var owner_id: Int,
    var title: String,
    var singer: String
)

data class RepostAttachment(
    var repost: Repost
) : Attachment {
    override val type: String = "repost"
}

data class Repost(
    var id: Int,
    var owner_id: Int,
    var title: String,
    var linkOwner: String
)

class PostNotFoundException(message: String) : RuntimeException(message)
object WallService {
    private var posts = emptyArray<Post>()
    private var countId: Int = 0

    //Метод для создания записи
    //добавлять запись в массив, но при этом назначать посту уникальный среди всех постов идентификатор;
    //возвращать пост с уже выставленным идентификатором.
    fun add(post: Post): Post {
        posts += post.copy(id = ++countId)

        //posts.set(countId, post)
        //countId += 1
        return posts.last()
    }


    //обновление записи
    //находить среди всех постов запись с тем же id, что и у post и обновлять все свойства;
    //если пост с таким id не найден, то ничего не происходит и возвращается false, в противном случае – возвращается true.

    /*
    изменять данные, пришедшие, через параметры метода не принято,
    так как это может стать неожиданностью для пользователей этого метода.
    Здесь желательно использовать встроенный метод классов данных copy
    (в параметрах метода указываются только те поля, которые вам нужно изменить).
     */
    fun update(post: Post): Boolean {
        for ((index, postAny) in posts.withIndex()) {
            if (postAny.id == post.id) {
                if (post.text != null && !post.text.equals("")) {
                    var postAnyT: Post = post.copy(
                        ownerId = post.ownerId, fromId = post.ownerId,
                        text = post.text, comments = post.comments, comment = post.comment,
                        likes = post.likes,
                        canPin = post.canPin, canDelete = post.canDelete, canEdit = post.canEdit,
                        isFavorite = post.isFavorite, arrayAttachments = post.arrayAttachments
                    )
                    posts.set(index, postAnyT)

                    return true
                }
            }
        }
        return false
    }


    fun findById(id: Int): Post? {
        for (post in posts) {
            if (post.id == id) {
                return post
            }
        }
        return null
    }

    private var comments = emptyArray<Comment>()
    private var countIdCom: Long = 0


    //  внедрим в сервис с постами возможность комментирования
    fun createComment(postId: Int, comment: Comment): Comment? {
        val post: Post = findById(postId) ?: throw PostNotFoundException("Пост не найден.")

        post.comments.setCount() //увеличим счетчик комментариев
        comments += comment.copy(id = ++countIdCom)
        post.comment = comments
        //countIdCom++
        return post.comment!!.last()

    }

    fun getPost(): Array<Post> {
        return posts
    }


    // сли вы хотите оставить WallService в виде object, то можно добавить в него метод очистки clear
    fun clear() {
        posts = emptyArray()
        comments = emptyArray()
        // также здесь нужно сбросить счетчик для id постов, если он у вас используется
        countId = 0
        countIdCom = 0
    }
}

data class Post(
    var id: Int = 0, // Идентификатор записи
    var ownerId: Int, // Идентификатор владельца стены, на которой размещена запись
    var fromId: Int, // Идентификатор автора записи
    var text: String?, // Текст записи
    var comments: Comments, // Информация о комментариях к записи, объект с полями
    var comment: Array<Comment>?, //
    var likes: Likes, // Информация о лайках к записи, объект с полями
    var canPin: Boolean, // Информация о том, может ли текущий пользователь закрепить запись (1 — может, 0 — не может)
    var canDelete: Boolean, // Информация о том, может ли текущий пользователь удалить запись (1 — может, 0 — не может)
    var canEdit: Boolean, // Информация о том, может ли текущий пользователь редактировать запись (1 — может, 0 — не может)
    var isFavorite: Boolean, // true, если объект добавлен в закладки у текущего пользователя
    var arrayAttachments: ArrayAttachments?
)

fun main() {
    val commentCounter1 = Comments()
    val like1 = Likes()
    like1.setCount()


    var post1 = Post(0, 1, 1, "Hello", commentCounter1, null, like1, true, true, true, true, null)

    val commentCounter2 = Comments()
    val like2 = Likes()
    var post2 = Post(0, 1, 2, "Hi", commentCounter2, null, like2, true, true, true, true, null)

    val ws = WallService
    ws.add(post1)
    ws.add(post2)


    val donut1 = Donut()
    val thread1 = Thread()
    var comment1 = Comment(0, 0, System.currentTimeMillis(), "Комментарий 1", donut1, 0, 0, null, null, thread1)
    println(ws.createComment(1, comment1))

    val photo1 = Photo(5, 11, "https://qweqeerw", "")
    val photoAttachment1 = PhotoAttachment(photo1)
    val photo2 = Photo(6, 11, "https://qwrw", "")
    val photoAttachment2 = PhotoAttachment(photo2)
    var arrayAttachments1 = ArrayAttachments()
    arrayAttachments1.addAttachment(photoAttachment1)
    arrayAttachments1.addAttachment(photoAttachment2)

    var post3 = Post(2, 3, 3, "Hi", commentCounter2, null, like2, true, true, true, true, arrayAttachments1)
    ws.update(post3)


    val newPosts = ws.getPost()
    for (index in newPosts.indices) {
        println("$index - ${newPosts[index]}")
    }
}