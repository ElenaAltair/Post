object Comments { // Информация о комментариях к записи, объект с полями
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

object Likes { //Информация о лайках к записи, объект с полями
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

object WallService {
    private var posts = emptyArray<Post>()
    private var countId: Int = 0

    //Метод для создания записи
    //добавлять запись в массив, но при этом назначать посту уникальный среди всех постов идентификатор;
    //возвращать пост с уже выставленным идентификатором.
    fun add(post: Post): Post {
        post.id = countId + 1;
        posts += post
        //posts.set(countId, post)
        countId += 1
        return posts.last()
    }

    //обновление записи
    //находить среди всех постов запись с тем же id, что и у post и обновлять все свойства;
    //если пост с таким id не найден, то ничего не происходит и возвращается false, в противном случае – возвращается true.
    fun update(post: Post): Boolean {
        for ((index, postAny) in posts.withIndex()) {
            if (postAny.id == post.id) {
                postAny.owner_id = post.owner_id
                postAny.from_id = post.from_id
                postAny.text = post.text
                postAny.comments = post.comments
                postAny.likes = post.likes
                postAny.can_pin = post.can_pin
                postAny.can_delete = post.can_delete
                postAny.can_edit = post.can_edit
                postAny.is_favorite = post.is_favorite
                return true
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
    var owner_id: Int, // Идентификатор владельца стены, на которой размещена запись
    var from_id: Int, // Идентификатор автора записи
    var text: String, // Текст записи
    var comments: Comments, // Информация о комментариях к записи, объект с полями
    var likes: Likes, // Информация о лайках к записи, объект с полями
    var can_pin: Boolean, // Информация о том, может ли текущий пользователь закрепить запись (1 — может, 0 — не может)
    var can_delete: Boolean, // Информация о том, может ли текущий пользователь удалить запись (1 — может, 0 — не может)
    var can_edit: Boolean, // Информация о том, может ли текущий пользователь редактировать запись (1 — может, 0 — не может)
    var is_favorite: Boolean // true, если объект добавлен в закладки у текущего пользователя
)

fun main() {
    val comment1 = Comments
    comment1.setCount()
    val like1 = Likes
    like1.setCount()
    var post1 = Post(0, 1, 1, "Hello", comment1, like1, true, true, true, true)

    val comment2 = Comments
    comment2.setCount()
    val like2 = Likes
    var post2 = Post(0, 1, 2, "Hi", comment2, like2, true, true, true, true)

    val ws = WallService
    ws.add(post1)
    ws.add(post2)

    var post3 = Post(2, 3, 3, "Hi", comment2, like2, true, true, true, true)
    ws.update(post3)

    val newPosts = ws.getPost()
    for (index in newPosts.indices) {
        println("$index - ${newPosts[index]}")
    }
}