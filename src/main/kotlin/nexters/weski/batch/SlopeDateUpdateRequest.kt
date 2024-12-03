package nexters.weski.batch

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class SlopeDateUpdateRequest (
    @Schema(
        description = "스키장 ID",
        example = "1"
    )
    val resortId: Long,

    @Schema(
        description = "슬로프 이름",
        example = "레몬1"
    )
    val slopeName: String,

    @Schema(
        description = "주간/야간/심야/새벽/자정 중 하나",
        example = "주간"
    )
    val timeType: String,

    @Schema(
        description = "운영 여부(Y/N)",
        example = "Y"
    )
    val isOpen: String,
)
