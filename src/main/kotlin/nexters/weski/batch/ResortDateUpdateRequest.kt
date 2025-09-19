package nexters.weski.batch

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class ResortDateUpdateRequest(
    @Schema(
        description = "스키장 ID",
        example = "1",
    )
    val resortId: Long,
    @Schema(
        description = "날짜 타입 (OPENING_DATE: 개장일, CLOSING_DATE: 폐장일)",
        example = "OPENING_DATE",
    )
    val dateType: DateType,
    @Schema(
        description = "날짜 (yyyy-MM-dd 형식)",
        example = "2024-11-30",
    )
    val date: LocalDate,
)

enum class DateType {
    @Schema(description = "개장일")
    OPENING_DATE,

    @Schema(description = "폐장일")
    CLOSING_DATE,
}
