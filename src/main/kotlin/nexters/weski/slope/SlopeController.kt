package nexters.weski.slope

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Tag(name = "스키장 별 슬로프&웹캠 정보 API", description = "슬로프&웹캠 관련")
@RestController
class SlopeController(
    private val slopeService: SlopeService
) {
    @Operation(summary = "특정 스키장의 슬로프와 웹캠 정보를 조회하는 API")
    @GetMapping("/api/slopes/{resortId}")
    fun getSlopesAndWebcams(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long
    ): SlopeResponseDto {
        return slopeService.getSlopesAndWebcams(resortId)
    }
}
