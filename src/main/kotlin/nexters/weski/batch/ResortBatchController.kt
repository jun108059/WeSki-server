package nexters.weski.batch

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import nexters.weski.ski_resort.SkiResortService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@Tag(name = "스키장 개장일/폐장일 업데이트 API", description = "스키장 개장일/폐장일을 업데이트")
@RestController
class ResortBatchController(
    private val resortService: SkiResortService
) {
    @Operation(
        summary = "스키장 개장일/폐장일 업데이트 API",
        description = """
            스키장 개장일을 업데이트하면 해당 스키장의 개장일이 변경됩니다.
            date : OPENING_DATE, CLOSING_DATE 중 하나를 선택합니다. 
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
    @PostMapping("/batch/resort-date")
    fun updateResortDate(
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "스키장 개장일/폐장일 업데이트 요청",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ResortDateUpdateRequest::class)
            )]
        )
        request: ResortDateUpdateRequest
    ) {
        resortService.updateResortDate(
            resortId = request.resortId,
            dateType = request.dateType,
            date = request.date
        )
    }

    @Operation(
        summary = "스키장 운영상태 업데이트",
        description = """
            스키장 운영상태를 업데이트하면 해당 스키장의 개장일과 폐장일을 기준으로 운영상태가 변경됩니다.
            스키장 운영상태는 다음과 같습니다.
            - 예정
            - 운영중
            - 운영종료
        """
    )
    @PostMapping("/batch/resort-status")
    fun updateResortStatus() {
        resortService.updateSkiResortStatus()
    }
}
