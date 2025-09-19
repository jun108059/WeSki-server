package nexters.weski.slope

data class SlopeDto(
    val slopeId: Long,
    val name: String,
    val difficulty: String,
    val isDayOperating: Boolean,
    val isNightOperating: Boolean,
    val isLateNightOperating: Boolean,
    val isDawnOperating: Boolean,
    val isMidnightOperating: Boolean,
    val webcamNo: Int?,
) {
    companion object {
        fun fromEntity(entity: Slope): SlopeDto =
            SlopeDto(
                slopeId = entity.id,
                name = entity.name,
                difficulty = entity.difficulty.name,
                isDayOperating = entity.isDayOperating,
                isNightOperating = entity.isNightOperating,
                isLateNightOperating = entity.isLateNightOperating,
                isDawnOperating = entity.isDawnOperating,
                isMidnightOperating = entity.isMidnightOperating,
                webcamNo = entity.webcamNumber,
            )
    }
}
