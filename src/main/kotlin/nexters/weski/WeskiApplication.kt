package nexters.weski

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["nexters.weski"])
@EnableScheduling
class WeskiApplication

fun main(args: Array<String>) {
    runApplication<WeskiApplication>(*args)
}
