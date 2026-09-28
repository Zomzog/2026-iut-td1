package iut.nantes

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import java.util.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class ExercisesTest {

    @Nested
    inner class Exo1 {

        @Test
        fun test() {
            val context = AnnotationConfigApplicationContext(AppConfig::class.java)
            val userService: UserService = context.getBean(UserService::class.java)
            userService.save(user())
            val user = userService.findOne(user().id)

            assertThat(user).isEqualTo(user())
        }
    }

    @Nested
    inner class Exo2 {

        @Test
        fun test() {
            val context = AnnotationConfigApplicationContext(AppConfig::class.java)
            val userService: UserService = context.getBean(UserService::class.java)
            val superUserService: SuperUserService = context.getBean(SuperUserService::class.java)
            userService.save(user())

            assertThat(superUserService.findAll()).isEqualTo(listOf(user()))
        }
    }

    @Nested
    inner class Exo3 {

        @Test
        fun test() {
            val context = AnnotationConfigApplicationContext(AppConfig::class.java)
            val userService: UserService = context.getBean(UserService::class.java)
            val superUserService: SuperUserService = context.getBean(SuperUserService::class.java)
            userService.save(user())

            assertThat(superUserService.findAll()).isEmpty()
        }
    }

    @Nested
    inner class Exo7 {

        @Test
        fun test() {
            val context = AnnotationConfigApplicationContext(AppConfig::class.java)
            val userService = context.getBean(UserService::class.java)
            val superUserService = context.getBean(SuperUserService::class.java)

            assertThat(userService.database).isInstanceOf(ListDatabase::class)
            assertThat(superUserService.database).isInstanceOf(HashDatabase::class)
        }
    }
}

fun user(uuid: UUID = UUID(0, 1)) = User(uuid, "John Doe", "email@noop.pony", 42)
