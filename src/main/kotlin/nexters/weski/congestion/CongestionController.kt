package nexters.weski.congestion

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Tag(name = "혼잡도 정보 조회 API", description = "특정 스키장의 혼잡도 정보 조회")
@RestController
class CongestionController(
    private val congestionService: CongestionService,
) {
    @Operation(summary = "특정 스키장의 혼잡도 조회 API")
    @GetMapping("/api/congestion/{resortId}")
    fun getResortCongestion(
        @PathVariable resortId: String,
    ): List<CongestionResponseDto> = congestionService.getCongestion(resortId)
}
