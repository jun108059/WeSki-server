package nexters.weski.weather

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort

@Entity
@Table(name = "hourly_weather")
data class HourlyWeather(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val forecastTime: String,
    val priority: Int,
    val temperature: Int,
    val precipitationChance: Int,
    @Column(name = "`condition`")
    val condition: String,

    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
