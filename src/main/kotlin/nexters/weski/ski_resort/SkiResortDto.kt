package nexters.weski.ski_resort

data class SkiResortDto(
    val resortId: Long,
    val name: String,
    val status: ResortStatus,
    val openingDate: String?,
    val closingDate: String?,
    val openSlopes: Int,
    val totalSlopes: Int
) {
    companion object {
        fun fromEntity(entity: SkiResort): SkiResortDto {
            return SkiResortDto(
                resortId = entity.resortId,
                name = entity.name,
                status = entity.status,
                openingDate = entity.openingDate?.toString(),
                closingDate = entity.closingDate?.toString(),
                openSlopes = entity.openSlopes,
                totalSlopes = entity.totalSlopes
            )
        }
    }
}
