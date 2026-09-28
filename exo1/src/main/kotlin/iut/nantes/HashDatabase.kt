package iut.nantes

import java.util.UUID

class HashDatabase: Database {

    val users: MutableMap<UUID, User> = mutableMapOf()


    override fun save(user: User) {
        users[user.id] = user
    }

    override fun delete(user: User) {
        users.remove(user.id)
    }

    override fun update(user: User) {
        users.computeIfPresent(user.id) { _, _ -> user }
    }

    override fun findOne(id: UUID): User? {
        return users[id]
    }

    override fun findAll(name: String?): List<User> =
        if (null != name) {
            users.values.filter { it.name == name }
        } else {
            users.values.toList()
        }
}