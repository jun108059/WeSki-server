package nexters.weski.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class SwaggerConfig {
    @Value("\${spring.profiles.active:local}")
    private lateinit var activeProfile: String

    @Value("\${server.port:8080}")
    private var serverPort: Int = 8080

    @Value("\${app.swagger.server-url.dev:}")
    private var devServerUrl: String = ""

    @Value("\${app.swagger.server-url.prod:}")
    private var prodServerUrl: String = ""

    @Value("\${app.swagger.server-url.local:}")
    private var localServerUrl: String = ""

    @Bean
    fun openAPI(): OpenAPI {
        val serverUrl = getServerUrl()

        return OpenAPI()
            .info(
                Info()
                    .title("We SKI API")
                    .description("we-ski app server API docs")
                    .version("1.0.0"),
            ).servers(
                listOf(
                    io.swagger.v3.oas.models.servers
                        .Server()
                        .url(serverUrl)
                        .description("${activeProfile.uppercase()} Environment"),
                ),
            )
    }

    private fun getServerUrl(): String =
        when (activeProfile.lowercase()) {
            "dev", "development" -> devServerUrl
            "prod", "production" -> prodServerUrl
            "local" -> localServerUrl.ifBlank { "http://localhost:$serverPort" }
            else -> "http://localhost:$serverPort"
        }

    @Bean
    fun userApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("API for WE-SKI Client")
            .pathsToMatch("/api/**")
            .build()

    @Bean
    fun productApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("BATCH for ADMIN")
            .pathsToMatch("/batch/**")
            .build()

    @Bean
    fun forwardedHeaderFilter(): ForwardedHeaderFilter = ForwardedHeaderFilter()
}
