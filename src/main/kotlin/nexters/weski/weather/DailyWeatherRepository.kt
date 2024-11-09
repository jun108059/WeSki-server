package nexters.weski.weather

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface DailyWeatherRepository : JpaRepository<DailyWeather, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<DailyWeather>
    fun deleteByDDayGreaterThanEqual(dDay: Int)
    fun deleteByDDay(dDay: Int)

    @Modifying
    @Query("UPDATE DailyWeather dw SET dw.dDay = dw.dDay - 1 WHERE dw.dDay > 0")
    fun decrementDDayValues()
}
