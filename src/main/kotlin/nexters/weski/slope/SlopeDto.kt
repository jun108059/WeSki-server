package nexters.weski.slope

data class SlopeDto(
    val name: String,
    val difficulty: String,
    val isDayOperating: Boolean,
    val isNightOperating: Boolean,
    val isLateNightOperating: Boolean,
    val isDawnOperating: Boolean,
    val isMidnightOperating: Boolean
) {
    companion object {
        fun fromEntity(entity: Slope): SlopeDto {
            return SlopeDto(
                name = entity.name,
                difficulty = entity.difficulty.name,
                isDayOperating = entity.isDayOperating,
                isNightOperating = entity.isNightOperating,
                isLateNightOperating = entity.isLateNightOperating,
                isDawnOperating = entity.isDawnOperating,
                isMidnightOperating = entity.isMidnightOperating
            )
        }
    }
}