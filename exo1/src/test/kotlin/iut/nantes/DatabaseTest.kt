package iut.nantes

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ListDatabaseTest : DatabaseTest({ ListDatabase() })
class HashDatabaseTest : DatabaseTest({ HashDatabase() })

abstract class DatabaseTest(val instance: () -> Database) {
    lateinit var database: Database
    @BeforeEach
    fun setup() {
        database = instance()
    }

    @Test
    fun `save puis findOne retrouve l'utilisateur`() {
        database.save(user())

        assertThat(database.findOne(user().id)).isEqualTo(user())
    }

    @Test
    fun `findOne renvoie null si l'id est inconnu`() {
        assertThat(database.findOne(UUID(0, 42))).isNull()
    }

    @Test
    fun `delete retire l'utilisateur`() {
        database.save(user())

        database.delete(user())

        assertThat(database.findOne(user().id)).isNull()
    }

    @Test
    fun `update remplace l'utilisateur`() {
        database.save(user())
        val updated = user().copy(name = "Jane Doe")

        database.update(updated)

        assertThat(database.findOne(user().id)).isEqualTo(updated)
    }

    @Test
    fun `update d'un utilisateur non existant`() {
        val updated = user().copy(name = "Jane Doe")

        database.update(updated)

        assertThat(database.findOne(user().id)).isNull()
    }

    @Test
    fun `findAll sans nom renvoie tous les utilisateurs`() {
        database.save(user())
        database.save(user(UUID(0, 2), "Jane Doe"))

        assertThat(database.findAll(null)).containsExactly(user(), user(UUID(0, 2), "Jane Doe"))
    }

    @Test
    fun `findAll filtre sur le nom`() {
        database.save(user())
        database.save(user(UUID(0, 2), "Jane Doe"))

        assertThat(database.findAll("Jane Doe")).containsExactly(user(UUID(0, 2), "Jane Doe"))
    }

    @Test
    fun `findAll renvoie une liste vide sur une base vide`() {
        assertThat(database.findAll(null)).isEmpty()
    }

    private fun user(uuid: UUID = UUID(0, 1), name: String = "John Doe") =
        User(uuid, name, "email@noop.pony", 42)
}