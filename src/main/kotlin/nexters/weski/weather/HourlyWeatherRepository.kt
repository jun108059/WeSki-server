package nexters.weski.weather

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface HourlyWeatherRepository : JpaRepository<HourlyWeather, Long> {
    fun findAllBySkiResortResortIdAndForecastTimeBetween(
        resortId: Long,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<HourlyWeather>
}
