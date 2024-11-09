package nexters.weski.congestion

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "스키장 혼잡도 응답 DTO")
data class CongestionResponseDto(
    val time: String,
    val congestion: Int,
    val description: String
) {
    companion object {
        fun fromEntity(congestion: Congestion): CongestionResponseDto {
            return CongestionResponseDto(
                time = congestion.time,
                congestion = congestion.congestion,
                description = congestion.description
            )
        }
    }
}
