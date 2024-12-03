package nexters.weski.batch

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import nexters.weski.ski_resort.SkiResortService
import nexters.weski.slope.SlopeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "슬로프 데이터 업데이트 API", description = "스키장 슬로프 데이터를 업데이트")
@RestController
class SlopeBatchController(
    private val slopeService: SlopeService,
    private val resortService: SkiResortService
) {
    @Operation(
        summary = "스키장 슬로프 운영 현황 업데이트 API",
        description = """
            스키장 id, 슬로프 이름, 주간/야간/심야/새벽/자정, 운영 여부를 파라미터로 전달해서 업데이트합니다.
            resortId는 다음과 같습니다.
            1, 지산 리조트
            2, 곤지암 스키장
            3, 비발디파크
            4, 엘리시안 강촌
            5, 웰리힐리파크
            6, 휘닉스파크
            7, 하이원 스키장
            8, 용평스키장 모나
            9, 무주덕유산
            10, 에덴벨리(양산)
            11, 오투리조트
        """
    )
    @PostMapping("/batch/slope-status")
    fun updateSlopeStatus(
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "slopes 운영여부 업데이트 요청",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = SlopeDateUpdateRequest::class)
            )]
        )
        request: SlopeDateUpdateRequest
    ) {
        slopeService.updateSlopeOpeningStatus(
            resortId = request.resortId,
            slopeName = request.slopeName,
            timeType = request.timeType,
            isOpen = request.isOpen
        )
    }

    @Operation(
        summary = "스키장 슬로프 count 업데이트",
        description = "스키장의 슬로프 count가 업데이트됩니다."
    )
    @PostMapping("/batch/resort-slope-count")
    fun updateSlopeCount() {
        resortService.updateSkiResortSlopeCount()
    }

}