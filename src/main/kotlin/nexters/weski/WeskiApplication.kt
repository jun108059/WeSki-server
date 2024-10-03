package nexters.weski

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["nexters.weski"])
class WeskiApplication

fun main(args: Array<String>) {
    runApplication<WeskiApplication>(*args)
}
