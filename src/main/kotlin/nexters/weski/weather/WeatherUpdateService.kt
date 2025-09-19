package nexters.weski.weather


import nexters.weski.ski.resort.SkiResortRepository
import org.springframework.stereotype.Service

@Service
class WeatherUpdateService(
    private val skiResortRepository: SkiResortRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
) {
//    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    fun updateWeatherData() {
        val skiResorts = skiResortRepository.findAll()
        skiResorts.forEach { resort ->
            println(resort)
            // 외부 API 호출하여 날씨 정보 가져오기
            // currentWeatherRepository.save(업데이트된 데이터)
        }
    }
}
