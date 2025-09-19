package nexters.weski.batch

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WeatherBatchController(
    private val externalWeatherService: ExternalWeatherService,
) {
    @GetMapping("/batch/current-weather")
    fun scheduleWeatherUpdate() {
        externalWeatherService.updateCurrentWeather()
    }

    @GetMapping("/batch/weekly-weather")
    fun scheduledDailyWeatherUpdate() {
        externalWeatherService.updateDailyWeather()
    }

    @GetMapping("/batch/hourly-weather")
    fun scheduledHourlyAndDailyUpdate() {
        externalWeatherService.updateHourlyAndDailyWeather()
    }
}
