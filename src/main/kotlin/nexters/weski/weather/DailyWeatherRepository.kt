package nexters.weski.weather

import nexters.weski.ski_resort.SkiResort
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DailyWeatherRepository : JpaRepository<DailyWeather, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<DailyWeather>
    fun deleteByDDayGreaterThanEqual(dDay: Int)
    fun findBySkiResortAndDDay(skiResort: SkiResort, dDay: Int): DailyWeather?
    fun findBySkiResortAndForecastDate(skiResort: SkiResort, forecastDate: LocalDate): List<DailyWeather>
}
