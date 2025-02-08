package nexters.weski.app_version

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "App 최소 버전 check API", description = "App 최소 버전 check API")
@RestController
class AppVersionCheckController(
    private val appVersionCheckService: AppVersionCheckService,
) {
    @Operation(summary = "App 버전 조회 API")
    @GetMapping("/api/app-version")
    fun getAppVersion(
        @Parameter(description = "사용자 App platform", example = "iOS")
        @RequestParam platform: String,
        @Parameter(description = "사용자 현재 App version", example = "1.0.0")
        @RequestParam version: String,
    ): AppVersionResponseDto {
        if (platform.uppercase() !in Platform.entries.map { it.name }) {
            throw IllegalArgumentException("platform must be one of ${Platform.entries.joinToString()}")
        }
        return appVersionCheckService.getAppVersion(
            platform = Platform.valueOf(platform.uppercase()),
            version = version,
        )
    }
}
