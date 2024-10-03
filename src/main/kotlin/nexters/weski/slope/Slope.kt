package nexters.weski.slope

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort

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

    val isDayOperating: Boolean = false,
    val isNightOperating: Boolean = false,
    val isLateNightOperating: Boolean = false,
    val isDawnOperating: Boolean = false,
    val isMidnightOperating: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()

enum class DifficultyLevel {
    초급, 중급, 중상급, 상급, 최상급, 파크
}