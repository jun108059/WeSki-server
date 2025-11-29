package nexters.weski.slope

import jakarta.validation.constraints.NotNull

data class UpdateSlopeRequest(
    @field:NotNull
    val name: String,
    @field:NotNull
    val difficulty: DifficultyLevel,
    val webcamNumber: Int?,
    val isDayOperating: Boolean,
    val isNightOperating: Boolean,
    val isLateNightOperating: Boolean,
    val isDawnOperating: Boolean,
    val isMidnightOperating: Boolean,
)
