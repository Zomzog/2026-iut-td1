package iut.nantes

import java.util.UUID

class ListDatabase : Database {

    private val users: MutableList<User> = mutableListOf()

    override fun save(user: User) {
       users.add(user)
    }

    override fun delete(user: User) {
        users.removeIf { it.id == user.id }
    }

    override fun update(user: User) {
        if (users.removeIf { it.id == user.id }) {
            users.add(user)
        }
    }

    override fun findOne(id: UUID): User? {
        return users.find { it.id == id }?.let { return it }
    }

    override fun findAll(name: String?): List<User> =
        if (null != name) {
            return users.filter { it.name == name }
        } else {
           return users
        }
}
