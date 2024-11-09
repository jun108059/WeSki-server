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
    val dDay: Int,
    val precipitationChance: Int,
    val maxTemp: Int,
    val minTemp: Int,
    @Column(name = "`condition`")
    val condition: String,

    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
