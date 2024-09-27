package nexters.weski.weather

import org.springframework.data.jpa.repository.JpaRepository

interface CurrentWeatherRepository : JpaRepository<CurrentWeather, Long> {
    fun findBySkiResortResortId(resortId: Long): CurrentWeather?
}
