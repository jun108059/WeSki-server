package nexters.weski.slope

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import nexters.weski.ski.resort.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Admin 슬로프 관리 API", description = "관리자용 슬로프 수정")
@RestController
@RequestMapping("/api/admin/slopes")
class AdminSlopeController(
    private val slopeService: SlopeService,
) {
    @Operation(summary = "슬로프 정보 수정", description = "슬로프 정보를 수정합니다")
    @PutMapping("/{slopeId}")
    fun updateSlope(
        @Parameter(description = "슬로프 ID", example = "1")
        @PathVariable slopeId: Long,
        @Valid @RequestBody request: UpdateSlopeRequest,
    ): ResponseEntity<ApiResponse<SlopeDto>> {
        val updatedSlope = slopeService.updateSlope(slopeId, request)
        return ResponseEntity.ok(
            ApiResponse.success("슬로프 정보가 성공적으로 수정되었습니다", updatedSlope),
        )
    }
}
