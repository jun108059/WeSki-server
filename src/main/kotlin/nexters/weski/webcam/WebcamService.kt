package nexters.weski.webcam

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WebcamService(
    private val webcamRepository: WebcamRepository,
) {
    @Transactional
    fun updateWebcam(
        webcamId: Long,
        request: UpdateWebcamRequest,
    ): WebcamDto {
        val webcam =
            webcamRepository.findById(webcamId).orElseThrow {
                IllegalArgumentException("Webcam not found with id: $webcamId")
            }

        // name은 non-nullable이므로 값이 제공된 경우에만 업데이트
        request.name?.let { webcam.name = it }
        
        // description과 url은 nullable이므로 request 값으로 업데이트 (null 포함)
        webcam.description = request.description
        webcam.url = request.url

        return WebcamDto.fromEntity(webcam)
    }
}
