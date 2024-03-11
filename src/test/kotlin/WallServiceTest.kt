import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Before
import kotlin.test.Test


class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun add() {
        val ws = WallService
        val comment = Comments()
        val like = Likes()
        var post = Post(0, 1, 1, "Hello", comment, null, like, true, true, true, true, null)

        val result: Boolean = ws.add(post).id != 0
        assertTrue(result)
    }


    //изменяем пост с существующим id, возвращается true
    @Test
    fun updateIdIsNotNull() {
        val ws = WallService
        val comment = Comments()
        val like = Likes()
        var post = Post(0, 1, 1, "Hello", comment, null, like, true, true, true, true, null)
        val id: Int = ws.add(post).id

        var post2 = Post(id, 3, 3, "Hello", comment, null, like, true, true, true, true, null)

        val result: Boolean = ws.update(post2)
        assertTrue(result)
    }

    //изменяем пост с не существующим id, возвращается false
    @Test
    fun updateIdIsNull() {
        val ws = WallService
        val comment = Comments()
        val like = Likes()
        var post = Post(0, 1, 1, "Hello", comment, null, like, true, true, true, true, null)
        val id: Int = ws.add(post).id

        var post2 = Post(id + 1, 3, 3, "Hello", comment, null, like, true, true, true, true, null)

        val result: Boolean = ws.update(post2)
        assertFalse(result)
    }


    // Функция выкидывает исключение, если была попытка добавить комментарий к несуществующему посту
    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {

        // здесь код с вызовом функции, которая должна выкинуть PostNotFoundException
        val ws = WallService
        val comment = Comments()
        val like = Likes()


        var post = Post(1, 1, 1, "Hello", comment, null, like, true, true, true, true, null)
        val id: Int = ws.add(post).id
        //println(post.toString())
        //println(id)

        val donut1 = Donut()
        val thread1 = Thread()
        var comment1 = Comment(1, 0, System.currentTimeMillis(), "Комментарий 1", donut1, 0, 0, null, null, thread1)

        ws.createComment(id + 1, comment1)
    }


    // Функция отрабатывает правильно, если добавляется комментарий к правильному посту
    @Test
    fun shouldThrow2() {

        // здесь код с вызовом функции, которая должна выкинуть PostNotFoundException
        val ws = WallService
        val comment = Comments()
        val like = Likes()

        var post = Post(1, 1, 1, "Hello", comment, null, like, true, true, true, true, null)
        val id: Int = ws.add(post).id

        val donut1 = Donut()
        val thread1 = Thread()
        var comment1 = Comment(1, 0, System.currentTimeMillis(), "Комментарий 1", donut1, 0, 0, null, null, thread1)


        Assert.assertEquals(comment1, ws.createComment(id, comment1))
    }

}