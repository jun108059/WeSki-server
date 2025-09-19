package nexters.weski.congestion

import org.springframework.stereotype.Service

@Service
class CongestionService(
    private val congestionRepository: CongestionRepository,
) {
    fun getCongestion(resortId: String): List<CongestionResponseDto> =
        congestionRepository
            .findAllBySkiResortResortId(resortId = resortId.toLong())
            .map { CongestionResponseDto.fromEntity(it) }
}
