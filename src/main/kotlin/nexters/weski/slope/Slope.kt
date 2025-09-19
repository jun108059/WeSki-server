package nexters.weski.slope

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import nexters.weski.common.BaseEntity
import nexters.weski.ski.resort.SkiResort

@Entity
@Table(name = "slopes")
data class Slope(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val webcamNumber: Int? = null,
    @Enumerated(EnumType.STRING)
    val difficulty: DifficultyLevel,
    var isDayOperating: Boolean = false,
    var isNightOperating: Boolean = false,
    var isLateNightOperating: Boolean = false,
    var isDawnOperating: Boolean = false,
    var isMidnightOperating: Boolean = false,
    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort,
) : BaseEntity()

enum class DifficultyLevel {
    초급,
    초중급,
    중급,
    중상급,
    상급,
    최상급,
    파크,
    익스트림,
}
