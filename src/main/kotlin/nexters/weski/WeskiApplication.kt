package nexters.weski

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class WeskiApplication

fun main(args: Array<String>) {
    runApplication<WeskiApplication>(*args)
}
