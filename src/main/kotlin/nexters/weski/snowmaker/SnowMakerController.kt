package nexters.weski.snowmaker

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "스키장 설질 관련 API", description = "설질 투표와 현황 조회 API")
@RestController
class SnowMakerController(
    private val snowMakerService: SnowMakerService,
) {
    @Operation(summary = "스키장 설질 정보 조회 API")
    @GetMapping("/api/snow-maker/{resortId}")
    fun getSnowMaker(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long,
    ): SnowMakerDto = snowMakerService.getSnowMaker(resortId)

    @Operation(summary = "스키장 설질 투표 API")
    @PostMapping("/api/snow-maker/{resortId}/vote")
    fun voteSnowMaker(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long,
        @Parameter(description = "설질의 긍정/부정 boolean value", example = "true")
        @RequestParam isPositive: Boolean,
    ) {
        snowMakerService.voteSnowMaker(resortId, isPositive)
    }
}
