package nexters.weski.ski.resort

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * Admin용 스키장 생성 요청 DTO
 */
@Schema(description = "스키장 생성 요청 DTO")
data class CreateSkiResortRequest(
    @field:NotBlank(message = "스키장 이름은 필수입니다")
    @Schema(description = "스키장 이름", example = "하이원 스키장")
    val name: String,
    @field:NotNull(message = "운영 상태는 필수입니다")
    @Schema(description = "운영 상태", example = "예정")
    val status: ResortStatus,
    @Schema(description = "개장일", example = "2024-12-06")
    val openingDate: LocalDate? = null,
    @Schema(description = "폐장일", example = "2025-03-15")
    val closingDate: LocalDate? = null,
    @Schema(description = "주간 운영시간", example = "09:00~16:00")
    val dayOperatingHours: String? = null,
    @Schema(description = "야간 운영시간", example = "18:00~22:00")
    val nightOperatingHours: String? = null,
    @Schema(description = "심야 운영시간", example = "22:00~24:00")
    val lateNightOperatingHours: String? = null,
    @Schema(description = "새벽 운영시간", example = "05:00~07:00")
    val dawnOperatingHours: String? = null,
    @Schema(description = "자정 운영시간", example = "00:00~03:00")
    val midnightOperatingHours: String? = null,
    @Schema(description = "정설 시간", example = "16:00~18:00")
    val snowfallTime: String? = null,
    @field:NotBlank(message = "X 좌표는 필수입니다")
    @Schema(description = "X 좌표 (위도 매핑값)", example = "92")
    val xCoordinate: String,
    @field:NotBlank(message = "Y 좌표는 필수입니다")
    @Schema(description = "Y 좌표 (경도 매핑값)", example = "120")
    val yCoordinate: String,
    @field:NotBlank(message = "세부 지역 코드는 필수입니다")
    @Schema(description = "세부 지역 코드", example = "11D10502")
    val detailedAreaCode: String,
    @field:NotBlank(message = "광역 지역 코드는 필수입니다")
    @Schema(description = "광역 지역 코드", example = "11D10000")
    val broadAreaCode: String,
)

/**
 * Admin용 스키장 수정 요청 DTO
 */
@Schema(description = "스키장 수정 요청 DTO")
data class UpdateSkiResortRequest(
    @Schema(description = "스키장 이름", example = "하이원 스키장")
    val name: String? = null,
    @Schema(description = "운영 상태", example = "운영중")
    val status: ResortStatus? = null,
    @Schema(description = "개장일", example = "2024-12-06")
    val openingDate: LocalDate? = null,
    @Schema(description = "폐장일", example = "2025-03-15")
    val closingDate: LocalDate? = null,
    @Schema(description = "주간 운영시간", example = "09:00~16:00")
    val dayOperatingHours: String? = null,
    @Schema(description = "야간 운영시간", example = "18:00~22:00")
    val nightOperatingHours: String? = null,
    @Schema(description = "심야 운영시간", example = "22:00~24:00")
    val lateNightOperatingHours: String? = null,
    @Schema(description = "새벽 운영시간", example = "05:00~07:00")
    val dawnOperatingHours: String? = null,
    @Schema(description = "자정 운영시간", example = "00:00~03:00")
    val midnightOperatingHours: String? = null,
    @Schema(description = "정설 시간", example = "16:00~18:00")
    val snowfallTime: String? = null,
    @Schema(description = "X 좌표 (위도 매핑값)", example = "92")
    val xCoordinate: String? = null,
    @Schema(description = "Y 좌표 (경도 매핑값)", example = "120")
    val yCoordinate: String? = null,
    @Schema(description = "세부 지역 코드", example = "11D10502")
    val detailedAreaCode: String? = null,
    @Schema(description = "광역 지역 코드", example = "11D10000")
    val broadAreaCode: String? = null,
)

/**
 * Admin용 스키장 상세 응답 DTO
 */
@Schema(description = "Admin용 스키장 상세 정보 응답 DTO")
data class AdminSkiResortResponse(
    val resortId: Long,
    val name: String,
    val status: String,
    val openingDate: String?,
    val closingDate: String?,
    val openSlopes: Int,
    val totalSlopes: Int,
    val dayOperatingHours: String?,
    val nightOperatingHours: String?,
    val lateNightOperatingHours: String?,
    val dawnOperatingHours: String?,
    val midnightOperatingHours: String?,
    val snowfallTime: String?,
    val xCoordinate: String,
    val yCoordinate: String,
    val detailedAreaCode: String,
    val broadAreaCode: String,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun fromEntity(skiResort: SkiResort): AdminSkiResortResponse =
            AdminSkiResortResponse(
                resortId = skiResort.resortId,
                name = skiResort.name,
                status = skiResort.status.name,
                openingDate = skiResort.openingDate?.toString(),
                closingDate = skiResort.closingDate?.toString(),
                openSlopes = skiResort.openSlopes,
                totalSlopes = skiResort.totalSlopes,
                dayOperatingHours = skiResort.dayOperatingHours,
                nightOperatingHours = skiResort.nightOperatingHours,
                lateNightOperatingHours = skiResort.lateNightOperatingHours,
                dawnOperatingHours = skiResort.dawnOperatingHours,
                midnightOperatingHours = skiResort.midnightOperatingHours,
                snowfallTime = skiResort.snowfallTime,
                xCoordinate = skiResort.xCoordinate,
                yCoordinate = skiResort.yCoordinate,
                detailedAreaCode = skiResort.detailedAreaCode,
                broadAreaCode = skiResort.broadAreaCode,
                createdAt = skiResort.createdAt.toString(),
                updatedAt = skiResort.updatedAt.toString(),
            )
    }
}

/**
 * 공통 API 응답 DTO
 */
@Schema(description = "API 응답 DTO")
data class ApiResponse<T>(
    @Schema(description = "성공 여부", example = "true")
    val success: Boolean,
    @Schema(description = "응답 메시지", example = "성공적으로 처리되었습니다")
    val message: String,
    @Schema(description = "응답 데이터")
    val data: T? = null,
) {
    companion object {
        fun <T> success(
            message: String,
            data: T? = null,
        ): ApiResponse<T> = ApiResponse(true, message, data)

        fun <T> error(message: String): ApiResponse<T> = ApiResponse(false, message)
    }
}
