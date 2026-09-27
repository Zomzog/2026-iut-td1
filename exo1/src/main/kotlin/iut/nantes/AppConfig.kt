package iut.nantes

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class AppConfig {
    @Bean @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun database(): Database = ListDatabase()

    @Bean
    fun userService(db: Database) = UserService(db)

    @Bean
    fun superUserService(db: Database) = SuperUserService(db)

}