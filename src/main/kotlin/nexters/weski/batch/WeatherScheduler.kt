package nexters.weski.batch

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WeatherScheduler(
    private val externalWeatherService: ExternalWeatherService
) {
    @Scheduled(cron = "0 0 * * * ?")
    fun scheduleWeatherUpdate() {
        externalWeatherService.updateCurrentWeather()
    }
}