package nexters.weski.weather

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/weather")
class WeatherController(
    private val weatherService: WeatherService
) {
    @GetMapping("/{resortId}")
    fun getWeatherByResortId(@PathVariable resortId: Long): WeatherDto? {
        return weatherService.getWeatherByResortId(resortId)
    }
}
