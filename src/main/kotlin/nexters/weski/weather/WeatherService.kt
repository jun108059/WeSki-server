package nexters.weski.weather

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WeatherService(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val hourlyWeatherRepository: HourlyWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository
) {
    fun getWeatherByResortId(resortId: Long): WeatherDto? {
        val currentWeather = currentWeatherRepository.findBySkiResortResortId(resortId) ?: return null
        val hourlyWeather = hourlyWeatherRepository.findBySkiResortResortId(resortId)

        val today = LocalDate.now()
        val after7Days = today.plusDays(7)

        val dailyWeather = dailyWeatherRepository.findAllBySkiResortResortIdAndForecastDateBetweenOrderByForecastDate(
            resortId = resortId,
            startDate = today,
            endDate = after7Days
        )

        return WeatherDto.fromEntities(currentWeather, hourlyWeather, dailyWeather)
    }
}
