package nexters.weski.weather

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import nexters.weski.common.BaseEntity
import nexters.weski.ski.resort.SkiResort

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
    val skiResort: SkiResort,
) : BaseEntity()
