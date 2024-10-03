package nexters.weski.weather

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Tag(name = "스키장 별 날씨 API", description = "스키장 별 날씨 상세 정보 API")
@RestController
class WeatherController(
    private val weatherService: WeatherService
) {
    @Operation(summary = "특정 스키장의 날씨 정보를 조회하는 API")
    @GetMapping("/api/weather/{resortId}")
    fun getWeatherByResortId(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long
    ): WeatherDto? {
        return weatherService.getWeatherByResortId(resortId)
    }
}
