package nexters.weski.ski_resort

import nexters.weski.weather.CurrentWeatherRepository
import nexters.weski.weather.DailyWeatherRepository
import org.springframework.stereotype.Service

@Service
class SkiResortService(
    private val skiResortRepository: SkiResortRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository
) {
    fun getAllSkiResortsAndWeather(): List<SkiResortResponseDto> {
        val skiResorts = skiResortRepository.findAll()
        return skiResorts.map { skiResort ->
            val currentWeather = currentWeatherRepository.findBySkiResortResortId(skiResort.resortId)
            val weeklyWeather = dailyWeatherRepository.findAllBySkiResortResortId(skiResort.resortId)

            SkiResortResponseDto.fromEntity(skiResort, currentWeather, weeklyWeather)
        }
    }
}
