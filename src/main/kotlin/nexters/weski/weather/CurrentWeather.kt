package nexters.weski.weather

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort

@Entity
@Table(name = "current_weather")
data class CurrentWeather(
    @Id
    val resortId: Long,

    val temperature: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val feelsLike: Int,
    val description: String,

    @Enumerated(EnumType.STRING)
    val condition: WeatherCondition,

    @OneToOne
    @MapsId
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
