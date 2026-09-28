package iut.nantes

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class Exo9Test {

    @Autowired
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var listDatabase: ListDatabase

    @Test
    fun test() {
        // GIVEN TODO
        every { listDatabase.delete(any()) } returns Unit
        every { listDatabase.delete(user()) } throws NoSuchElementException()

        // THEN
        assertThrows<NoSuchElementException> { userService.delete(user()) }
        userService.delete(user(UUID.randomUUID()))
    }
}