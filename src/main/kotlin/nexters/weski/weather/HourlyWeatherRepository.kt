package nexters.weski.weather

import nexters.weski.ski_resort.SkiResort
import org.springframework.data.jpa.repository.JpaRepository

interface HourlyWeatherRepository : JpaRepository<HourlyWeather, Long> {
    fun deleteBySkiResort(skiResort: SkiResort)
    fun findBySkiResortResortId(resortId: Long): List<HourlyWeather>
}
