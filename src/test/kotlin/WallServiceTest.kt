import org.junit.Before
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
        var post = Post(0, 1, 1, "Hello", comment, like, true, true, true, true, null)

        val result: Boolean = ws.add(post).id != 0
        assertTrue(result)
    }


    //изменяем пост с существующим id, возвращается true
    @Test
    fun updateIdIsNotNull() {
        val ws = WallService
        val comment = Comments()
        val like = Likes()
        var post = Post(0, 1, 1, "Hello", comment, like, true, true, true, true, null)
        val id: Int = ws.add(post).id

        var post2 = Post(id, 3, 3, "Hello", comment, like, true, true, true, true, null)

        val result: Boolean = ws.update(post2)
        assertTrue(result)
    }

    //изменяем пост с не существующим id, возвращается false
    @Test
    fun updateIdIsNull() {
        val ws = WallService
        val comment = Comments()
        val like = Likes()
        var post = Post(0, 1, 1, "Hello", comment, like, true, true, true, true, null)
        val id: Int = ws.add(post).id

        var post2 = Post(id + 1, 3, 3, "Hello", comment, like, true, true, true, true, null)

        val result: Boolean = ws.update(post2)
        assertFalse(result)
    }

}