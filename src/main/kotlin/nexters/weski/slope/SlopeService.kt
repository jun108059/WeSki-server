package nexters.weski.slope

import nexters.weski.ski.resort.SkiResortRepository
import nexters.weski.webcam.WebcamRepository
import org.springframework.stereotype.Service

@Service
class SlopeService(
    private val skiResortRepository: SkiResortRepository,
    private val slopeRepository: SlopeRepository,
    private val webcamRepository: WebcamRepository,
) {
    fun getSlopesAndWebcams(resortId: Long): SlopeResponseDto {
        val skiResort = skiResortRepository.findById(resortId).orElseThrow { Exception("Resort not found") }
        val slopes = slopeRepository.findAllBySkiResortResortId(resortId)
        val webcams = webcamRepository.findAllBySkiResortResortId(resortId)

        return SlopeResponseDto.fromEntities(skiResort, slopes, webcams)
    }

    fun getTotalSlopeCount(resortId: Long): Int = slopeRepository.countBySkiResortResortId(resortId)

    fun getOpeningSlopeCount(resortId: Long): Int = slopeRepository.countOperatingSlopesByResortId(resortId)

    fun updateSlopeOpeningStatus(
        resortId: Long,
        slopeName: String,
        timeType: String,
        isOpen: String,
    ) {
        val slope =
            slopeRepository.findBySkiResortResortIdAndName(resortId, slopeName)
                ?: throw Exception("Slope not found")

        when (timeType) {
            "주간" -> slope.isDayOperating = isOpen == "Y"
            "야간" -> slope.isNightOperating = isOpen == "Y"
            "심야" -> slope.isLateNightOperating = isOpen == "Y"
            "새벽" -> slope.isDawnOperating = isOpen == "Y"
            "자정" -> slope.isMidnightOperating = isOpen == "Y"
            else -> throw Exception("Invalid time type")
        }
        slopeRepository.save(slope)
    }

    fun updateSlope(
        slopeId: Long,
        request: UpdateSlopeRequest,
    ): SlopeDto {
        val slope =
            slopeRepository
                .findById(slopeId)
                .orElseThrow { Exception("Slope not found") }

        slope.name = request.name
        slope.difficulty = request.difficulty
        slope.webcamNumber = request.webcamNumber
        slope.isDayOperating = request.isDayOperating
        slope.isNightOperating = request.isNightOperating
        slope.isLateNightOperating = request.isLateNightOperating
        slope.isDawnOperating = request.isDawnOperating
        slope.isMidnightOperating = request.isMidnightOperating

        return SlopeDto.fromEntity(slopeRepository.save(slope))
    }
}
