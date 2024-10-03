package nexters.weski.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("We-ski API")
                    .description("we-ski server API")
                    .version("1.0.0")
            )
            .servers(
                listOf(
                    io.swagger.v3.oas.models.servers.Server().url("http://localhost:8080"),
                    io.swagger.v3.oas.models.servers.Server().url("http://223.130.154.51:8080")
                )
            )
    }

    @Bean
    fun forwardedHeaderFilter(): ForwardedHeaderFilter {
        return ForwardedHeaderFilter()
    }
}
