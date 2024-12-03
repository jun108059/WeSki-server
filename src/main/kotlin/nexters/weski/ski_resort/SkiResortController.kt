package nexters.weski.ski_resort

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Tag(name = "스키장 정보 API", description = "전체 스키장 날씨 및 슬로프 정보 관련")
@RestController
class SkiResortController(
    private val skiResortService: SkiResortService
) {
    @Operation(summary = "날씨와 슬로프 정보를 포함한 전체 스키장 데이터를 조회하는 API")
    @GetMapping("/api/ski-resorts")
    fun getAllSkiResorts(): List<SkiResortResponseDto> {
        return skiResortService.getAllSkiResortsAndWeather()
    }

    @Operation(summary = "날씨와 슬로프 정보를 포함한 특정 스키장 데이터를 조회하는 API")
    @GetMapping("/api/ski-resort/{resortId}")
    fun getSkiResort(
        @Parameter(description = "스키장 ID", example = "1")
        @PathVariable resortId: Long
    ): SkiResortResponseDto {
        return skiResortService.getSkiResortAndWeather(resortId)
    }
}
