package nexters.weski.weather

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort

@Entity
@Table(name = "current_weather")
data class CurrentWeather(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val temperature: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val feelsLike: Int,
    val description: String,

    @Column(name = "`condition`")
    val condition: String,

    @OneToOne
    @JoinColumn(name = "resort_id", unique = true)
    val skiResort: SkiResort
) : BaseEntity()
