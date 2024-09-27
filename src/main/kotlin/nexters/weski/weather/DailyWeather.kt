package nexters.weski.weather

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort
import java.time.LocalDate

@Entity
@Table(name = "daily_weather")
data class DailyWeather(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val forecastDate: LocalDate,
    val dayOfWeek: String,
    val precipitationChance: Int,
    val maxTemp: Int,
    val minTemp: Int,

    @Enumerated(EnumType.STRING)
    val dayCondition: WeatherCondition,

    @Enumerated(EnumType.STRING)
    val nightCondition: WeatherCondition,

    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
