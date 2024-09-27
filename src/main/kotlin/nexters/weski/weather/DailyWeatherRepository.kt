package nexters.weski.weather

import org.springframework.data.jpa.repository.JpaRepository

interface DailyWeatherRepository : JpaRepository<DailyWeather, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<DailyWeather>
}
