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
    //Можно перенести описание свойства type в наследниках в тело класса,
    // чтобы не надо было его задавать при создании экземпляров классов
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
    //Можно перенести описание свойства type в наследниках в тело класса,
    // чтобы не надо было его задавать при создании экземпляров классов
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
    //Можно перенести описание свойства type в наследниках в тело класса,
    // чтобы не надо было его задавать при создании экземпляров классов
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
    //Можно перенести описание свойства type в наследниках в тело класса,
    // чтобы не надо было его задавать при создании экземпляров классов
    override val type: String = "repost"
}

data class Repost(
    var id: Int,
    var owner_id: Int,
    var title: String,
    var linkOwner: String
)


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
                        text = post.text, comments = post.comments, likes = post.likes,
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

    fun getPost(): Array<Post> {
        return posts
    }


    // сли вы хотите оставить WallService в виде object, то можно добавить в него метод очистки clear
    fun clear() {
        posts = emptyArray()
        // также здесь нужно сбросить счетчик для id постов, если он у вас используется
        countId = 0
    }
}

data class Post(
    var id: Int = 0, // Идентификатор записи
    var ownerId: Int, // Идентификатор владельца стены, на которой размещена запись
    var fromId: Int, // Идентификатор автора записи
    var text: String?, // Текст записи
    var comments: Comments, // Информация о комментариях к записи, объект с полями
    var likes: Likes, // Информация о лайках к записи, объект с полями
    var canPin: Boolean, // Информация о том, может ли текущий пользователь закрепить запись (1 — может, 0 — не может)
    var canDelete: Boolean, // Информация о том, может ли текущий пользователь удалить запись (1 — может, 0 — не может)
    var canEdit: Boolean, // Информация о том, может ли текущий пользователь редактировать запись (1 — может, 0 — не может)
    var isFavorite: Boolean, // true, если объект добавлен в закладки у текущего пользователя
    var arrayAttachments: ArrayAttachments?
)

fun main() {
    val comment1 = Comments()
    comment1.setCount()
    val like1 = Likes()
    like1.setCount()
    var post1 = Post(0, 1, 1, "Hello", comment1, like1, true, true, true, true, null)

    val comment2 = Comments()
    comment2.setCount()
    val like2 = Likes()
    var post2 = Post(0, 1, 2, "Hi", comment2, like2, true, true, true, true, null)

    val ws = WallService
    ws.add(post1)
    ws.add(post2)

    val photo1 = Photo(5, 11, "https://qweqeerw", "")
    val photoAttachment1 = PhotoAttachment(photo1)
    val photo2 = Photo(6, 11, "https://qwrw", "")
    val photoAttachment2 = PhotoAttachment(photo2)
    var arrayAttachments1 = ArrayAttachments()
    arrayAttachments1.addAttachment(photoAttachment1)
    arrayAttachments1.addAttachment(photoAttachment2)

    var post3 = Post(2, 3, 3, "Hi", comment2, like2, true, true, true, true, arrayAttachments1)
    ws.update(post3)


    val newPosts = ws.getPost()
    for (index in newPosts.indices) {
        println("$index - ${newPosts[index]}")
    }
}