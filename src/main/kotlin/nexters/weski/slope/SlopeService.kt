package nexters.weski.slope

import nexters.weski.ski_resort.SkiResortRepository
import nexters.weski.webcam.WebcamRepository
import org.springframework.stereotype.Service

@Service
class SlopeService(
    private val skiResortRepository: SkiResortRepository,
    private val slopeRepository: SlopeRepository,
    private val webcamRepository: WebcamRepository
) {
    fun getSlopesAndWebcams(resortId: Long): SlopeResponseDto {
        val skiResort = skiResortRepository.findById(resortId).orElseThrow { Exception("Resort not found") }
        val slopes = slopeRepository.findAllBySkiResortResortId(resortId)
        val webcams = webcamRepository.findAllBySkiResortResortId(resortId)

        return SlopeResponseDto.fromEntities(skiResort, slopes, webcams)
    }
}