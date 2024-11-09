package nexters.weski.weather

import org.springframework.stereotype.Service

@Service
class WeatherService(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val hourlyWeatherRepository: HourlyWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository
) {
    fun getWeatherByResortId(resortId: Long): WeatherDto? {
        val currentWeather = currentWeatherRepository.findBySkiResortResortId(resortId) ?: return null
        val hourlyWeather = hourlyWeatherRepository.findBySkiResortResortId(resortId)
        val dailyWeather = dailyWeatherRepository.findAllBySkiResortResortId(resortId)

        return WeatherDto.fromEntities(currentWeather, hourlyWeather, dailyWeather)
    }
}
