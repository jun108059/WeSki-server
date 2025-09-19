package nexters.weski.weather

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import nexters.weski.common.BaseEntity
import nexters.weski.ski.resort.SkiResort

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
    val skiResort: SkiResort,
) : BaseEntity()
