package iut.nantes

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
@ComponentScan(basePackages = ["iut.nantes"])
class AppConfig {

    @Bean(HASH_DATABASE) @Scope(BeanDefinition.SCOPE_SINGLETON)
    fun hashDatabase() = HashDatabase()

    @Bean
    fun userService(db: Database) = UserService(db)

    @Bean
    fun superUserService() = SuperUserService()

    companion object {
        const val HASH_DATABASE = "hashDatabase"
    }
}