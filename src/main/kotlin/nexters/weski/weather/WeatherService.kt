package nexters.weski.weather

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class WeatherService(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val hourlyWeatherRepository: HourlyWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository
) {
    fun getWeatherByResortId(resortId: Long): WeatherDto? {
        val currentWeather = currentWeatherRepository.findBySkiResortResortId(resortId) ?: return null
        // 오늘, 내일 날짜의 날씨 정보만 가져옴
        val startTime = LocalDateTime.now().with(LocalTime.MIN)
        val endTime = startTime.plusDays(2).with(LocalTime.MAX)
        val hourlyWeather = hourlyWeatherRepository.findAllBySkiResortResortIdAndForecastTimeBetween(
            resortId, startTime, endTime
        )
        val dailyWeather = dailyWeatherRepository.findAllBySkiResortResortId(resortId)

        return WeatherDto.fromEntities(currentWeather, hourlyWeather, dailyWeather)
    }
}
