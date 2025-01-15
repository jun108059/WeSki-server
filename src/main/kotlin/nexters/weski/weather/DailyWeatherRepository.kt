package nexters.weski.weather

import nexters.weski.ski_resort.SkiResort
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DailyWeatherRepository : JpaRepository<DailyWeather, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<DailyWeather>
    fun findBySkiResortAndForecastDate(skiResort: SkiResort, forecastDate: LocalDate): DailyWeather?
    fun findAllBySkiResortResortIdAndForecastDateBetween(
        resortId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyWeather>
}
