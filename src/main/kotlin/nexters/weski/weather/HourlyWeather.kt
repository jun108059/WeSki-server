package nexters.weski.weather

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort
import java.time.LocalDateTime

@Entity
@Table(name = "hourly_weather")
data class HourlyWeather(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val forecastTime: LocalDateTime,
    val temperature: Int,
    val precipitationChance: Int,
    val condition: String,

    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
