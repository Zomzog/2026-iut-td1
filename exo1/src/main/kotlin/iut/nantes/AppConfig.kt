package iut.nantes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["iut.nantes"])
class AppConfig {

    @Bean
    fun userService(db: Database) = UserService(db)

    @Bean
    fun superUserService() = SuperUserService()

}