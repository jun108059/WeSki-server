package nexters.weski.ski_resort

import org.springframework.stereotype.Service

@Service
class SkiResortService(
    private val skiResortRepository: SkiResortRepository
) {
    fun getAllSkiResorts(): List<SkiResortDto> {
        return skiResortRepository.findAll().map { SkiResortDto.fromEntity(it) }
    }
}
