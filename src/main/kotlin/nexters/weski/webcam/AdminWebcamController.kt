package nexters.weski.webcam

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

@Tag(name = "Admin 웹캠 관리 API", description = "관리자용 웹캠 수정")
@RestController
@RequestMapping("/api/admin/webcams")
class AdminWebcamController(
    private val webcamService: WebcamService,
) {
    @Operation(summary = "웹캠 정보 수정", description = "웹캠 URL을 수정합니다")
    @PutMapping("/{webcamId}")
    fun updateWebcam(
        @Parameter(description = "웹캠 ID", example = "1")
        @PathVariable webcamId: Long,
        @Valid @RequestBody request: UpdateWebcamRequest,
    ): ResponseEntity<ApiResponse<WebcamDto>> {
        val updatedWebcam = webcamService.updateWebcam(webcamId, request)
        return ResponseEntity.ok(
            ApiResponse.success("웹캠 정보가 성공적으로 수정되었습니다", updatedWebcam),
        )
    }
}
