package nexters.weski.ski.resort

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Admin 스키장 관리 API", description = "관리자용 스키장 CRUD 작업")
@RestController
@RequestMapping("/api/admin/ski-resorts")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:5173"]) // React 개발 서버용
class AdminSkiResortController(
    private val adminSkiResortService: AdminSkiResortService,
) {
    @Operation(summary = "모든 스키장 목록 조회 (관리자용)", description = "관리자가 볼 수 있는 상세한 스키장 정보를 모두 조회합니다")
    @GetMapping
    fun getAllSkiResorts(): ResponseEntity<ApiResponse<List<AdminSkiResortResponse>>> {
        val skiResorts = adminSkiResortService.getAllSkiResorts()
        return ResponseEntity.ok(
            ApiResponse.success("스키장 목록을 성공적으로 조회했습니다", skiResorts),
        )
    }

    @Operation(summary = "특정 스키장 상세 조회 (관리자용)", description = "관리자용 상세한 스키장 정보를 조회합니다")
    @GetMapping("/{resortId}")
    fun getSkiResort(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long,
    ): ResponseEntity<ApiResponse<AdminSkiResortResponse>> {
        val skiResort = adminSkiResortService.getSkiResort(resortId)
        return ResponseEntity.ok(
            ApiResponse.success("스키장 정보를 성공적으로 조회했습니다", skiResort),
        )
    }

    @Operation(summary = "새 스키장 생성", description = "새로운 스키장을 생성합니다")
    @PostMapping
    fun createSkiResort(
        @Valid @RequestBody request: CreateSkiResortRequest,
    ): ResponseEntity<ApiResponse<AdminSkiResortResponse>> {
        val createdSkiResort = adminSkiResortService.createSkiResort(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("스키장이 성공적으로 생성되었습니다", createdSkiResort),
        )
    }

    @Operation(summary = "스키장 정보 수정", description = "기존 스키장의 정보를 수정합니다")
    @PutMapping("/{resortId}")
    fun updateSkiResort(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long,
        @Valid @RequestBody request: UpdateSkiResortRequest,
    ): ResponseEntity<ApiResponse<AdminSkiResortResponse>> {
        val updatedSkiResort = adminSkiResortService.updateSkiResort(resortId, request)
        return ResponseEntity.ok(
            ApiResponse.success("스키장 정보가 성공적으로 수정되었습니다", updatedSkiResort),
        )
    }

    @Operation(summary = "스키장 삭제", description = "스키장을 삭제합니다")
    @DeleteMapping("/{resortId}")
    fun deleteSkiResort(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long,
    ): ResponseEntity<ApiResponse<Unit>> {
        adminSkiResortService.deleteSkiResort(resortId)
        return ResponseEntity.ok(
            ApiResponse.success<Unit>("스키장이 성공적으로 삭제되었습니다"),
        )
    }

    @Operation(summary = "스키장 상태 일괄 업데이트", description = "모든 스키장의 운영 상태를 현재 날짜 기준으로 업데이트합니다")
    @PostMapping("/batch/update-status")
    fun updateAllResortStatus(): ResponseEntity<ApiResponse<Unit>> {
        adminSkiResortService.updateAllResortStatus()
        return ResponseEntity.ok(
            ApiResponse.success<Unit>("모든 스키장 상태가 성공적으로 업데이트되었습니다"),
        )
    }

    @Operation(summary = "스키장 슬로프 수 일괄 업데이트", description = "모든 스키장의 슬로프 수를 실제 데이터 기준으로 업데이트합니다")
    @PostMapping("/batch/update-slope-count")
    fun updateAllSlopeCount(): ResponseEntity<ApiResponse<Unit>> {
        adminSkiResortService.updateAllSlopeCount()
        return ResponseEntity.ok(
            ApiResponse.success<Unit>("모든 스키장 슬로프 수가 성공적으로 업데이트되었습니다"),
        )
    }
}
