package nexters.weski.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("We SKI API")
                    .description("we-ski app server API docs")
                    .version("1.0.0"),
            ).servers(
                listOf(
                    io.swagger.v3.oas.models.servers
                        .Server()
                        .url("http://223.130.154.51:8080"),
                ),
            )

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
