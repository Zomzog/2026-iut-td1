package iut.nantes

import assertk.assertThat
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Exo8Test {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var superUserService: SuperUserService

    @Test
    fun contextLoads() {
        // Test le chargement du contexte
    }


    @Test
    fun test() {
        assertThat(userService.database).isInstanceOf(ListDatabase::class)
        assertThat(superUserService.database).isInstanceOf(HashDatabase::class)
    }
}
