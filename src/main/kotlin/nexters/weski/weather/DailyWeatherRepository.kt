package nexters.weski.weather

import nexters.weski.ski.resort.SkiResort
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DailyWeatherRepository : JpaRepository<DailyWeather, Long> {
    fun findBySkiResortAndForecastDateBetweenOrderByForecastDate(
        skiResort: SkiResort,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyWeather>

    fun findBySkiResortAndForecastDate(
        skiResort: SkiResort,
        forecastDate: LocalDate,
    ): DailyWeather?

    fun findAllBySkiResortResortIdAndForecastDateBetweenOrderByForecastDate(
        resortId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyWeather>
}
