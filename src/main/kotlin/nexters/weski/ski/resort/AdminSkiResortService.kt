package nexters.weski.ski.resort

import nexters.weski.slope.SlopeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class AdminSkiResortService(
    private val skiResortRepository: SkiResortRepository,
    private val slopeService: SlopeService,
) {
    /**
     * 모든 스키장 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    fun getAllSkiResorts(): List<AdminSkiResortResponse> =
        skiResortRepository
            .findAllByOrderByResortIdAsc()
            .map { AdminSkiResortResponse.fromEntity(it) }

    /**
     * 특정 스키장 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    fun getSkiResort(resortId: Long): AdminSkiResortResponse {
        val skiResort =
            skiResortRepository
                .findById(resortId)
                .orElseThrow { IllegalArgumentException("ID $resortId 에 해당하는 스키장을 찾을 수 없습니다") }
        return AdminSkiResortResponse.fromEntity(skiResort)
    }

    /**
     * 새 스키장 생성
     */
    fun createSkiResort(request: CreateSkiResortRequest): AdminSkiResortResponse {
        // 동일한 이름의 스키장이 있는지 확인
        if (skiResortRepository.existsByName(request.name)) {
            throw IllegalArgumentException("이미 존재하는 스키장 이름입니다: ${request.name}")
        }

        val skiResort =
            SkiResort(
                name = request.name,
                status = request.status,
                openingDate = request.openingDate,
                closingDate = request.closingDate,
                dayOperatingHours = request.dayOperatingHours,
                nightOperatingHours = request.nightOperatingHours,
                lateNightOperatingHours = request.lateNightOperatingHours,
                dawnOperatingHours = request.dawnOperatingHours,
                midnightOperatingHours = request.midnightOperatingHours,
                snowfallTime = request.snowfallTime,
                xCoordinate = request.xCoordinate,
                yCoordinate = request.yCoordinate,
                detailedAreaCode = request.detailedAreaCode,
                broadAreaCode = request.broadAreaCode,
            )

        val savedSkiResort = skiResortRepository.save(skiResort)
        return AdminSkiResortResponse.fromEntity(savedSkiResort)
    }

    /**
     * 스키장 정보 수정
     */
    fun updateSkiResort(
        resortId: Long,
        request: UpdateSkiResortRequest,
    ): AdminSkiResortResponse {
        val existingSkiResort =
            skiResortRepository
                .findById(resortId)
                .orElseThrow { IllegalArgumentException("ID $resortId 에 해당하는 스키장을 찾을 수 없습니다") }

        // 이름 중복 체크 (다른 스키장과 중복되는지)
        request.name?.let { newName ->
            if (newName != existingSkiResort.name && skiResortRepository.existsByName(newName)) {
                throw IllegalArgumentException("이미 존재하는 스키장 이름입니다: $newName")
            }
        }

        val updatedSkiResort =
            existingSkiResort.copy(
                name = request.name ?: existingSkiResort.name,
                status = request.status ?: existingSkiResort.status,
                openingDate = request.openingDate ?: existingSkiResort.openingDate,
                closingDate = request.closingDate ?: existingSkiResort.closingDate,
                dayOperatingHours = request.dayOperatingHours ?: existingSkiResort.dayOperatingHours,
                nightOperatingHours = request.nightOperatingHours ?: existingSkiResort.nightOperatingHours,
                lateNightOperatingHours = request.lateNightOperatingHours ?: existingSkiResort.lateNightOperatingHours,
                dawnOperatingHours = request.dawnOperatingHours ?: existingSkiResort.dawnOperatingHours,
                midnightOperatingHours = request.midnightOperatingHours ?: existingSkiResort.midnightOperatingHours,
                snowfallTime = request.snowfallTime ?: existingSkiResort.snowfallTime,
                xCoordinate = request.xCoordinate ?: existingSkiResort.xCoordinate,
                yCoordinate = request.yCoordinate ?: existingSkiResort.yCoordinate,
                detailedAreaCode = request.detailedAreaCode ?: existingSkiResort.detailedAreaCode,
                broadAreaCode = request.broadAreaCode ?: existingSkiResort.broadAreaCode,
            )

        val savedSkiResort = skiResortRepository.save(updatedSkiResort)
        return AdminSkiResortResponse.fromEntity(savedSkiResort)
    }

    /**
     * 스키장 삭제
     */
    fun deleteSkiResort(resortId: Long) {
        val skiResort =
            skiResortRepository
                .findById(resortId)
                .orElseThrow { IllegalArgumentException("ID $resortId 에 해당하는 스키장을 찾을 수 없습니다") }

        // 관련 데이터가 있는지 확인 (슬로프, 웹캠 등)
        if (skiResort.slopes.isNotEmpty()) {
            throw IllegalStateException("슬로프 데이터가 있는 스키장은 삭제할 수 없습니다. 먼저 슬로프를 삭제해주세요.")
        }

        if (skiResort.webcams.isNotEmpty()) {
            throw IllegalStateException("웹캠 데이터가 있는 스키장은 삭제할 수 없습니다. 먼저 웹캠을 삭제해주세요.")
        }

        skiResortRepository.delete(skiResort)
    }

    /**
     * 모든 스키장 상태 업데이트
     */
    fun updateAllResortStatus() {
        val skiResorts = skiResortRepository.findAll()
        val today = LocalDate.now()

        skiResorts.forEach { skiResort ->
            val openingDate = skiResort.openingDate
            val closingDate = skiResort.closingDate

            val newStatus =
                when {
                    openingDate != null && today.isBefore(openingDate) -> ResortStatus.예정
                    closingDate != null && today.isAfter(closingDate) -> ResortStatus.운영종료
                    else -> ResortStatus.운영중
                }

            if (newStatus != skiResort.status) {
                val updatedSkiResort = skiResort.copy(status = newStatus)
                skiResortRepository.save(updatedSkiResort)
            }
        }
    }

    /**
     * 모든 스키장 슬로프 수 업데이트
     */
    fun updateAllSlopeCount() {
        val skiResorts = skiResortRepository.findAll()
        skiResorts.forEach { skiResort ->
            val totalSlopeCount = slopeService.getTotalSlopeCount(skiResort.resortId)
            val openingSlopeCount = slopeService.getOpeningSlopeCount(skiResort.resortId)

            if (totalSlopeCount != skiResort.totalSlopes || openingSlopeCount != skiResort.openSlopes) {
                val updatedSkiResort =
                    skiResort.copy(
                        totalSlopes = totalSlopeCount,
                        openSlopes = openingSlopeCount,
                    )
                skiResortRepository.save(updatedSkiResort)
            }
        }
    }
}
