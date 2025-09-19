package nexters.weski.weather

import nexters.weski.ski.resort.SkiResort
import org.springframework.data.jpa.repository.JpaRepository

interface HourlyWeatherRepository : JpaRepository<HourlyWeather, Long> {
    fun deleteBySkiResort(skiResort: SkiResort)

    fun findBySkiResortResortId(resortId: Long): List<HourlyWeather>
}
