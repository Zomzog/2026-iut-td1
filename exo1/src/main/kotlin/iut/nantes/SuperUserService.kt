package iut.nantes

import iut.nantes.AppConfig.Companion.HASH_DATABASE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

class SuperUserService {

    @Autowired
    @Qualifier(HASH_DATABASE)
    lateinit var database: Database
    fun findAll(): List<User> {
        return database.findAll(null)
    }
}