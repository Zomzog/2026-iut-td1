package iut.nantes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun database(): Database = ListDatabase()

    @Bean
    fun userService(db: Database) = UserService(db)

    @Bean
    fun superUserService(db: Database) = SuperUserService(db)

}