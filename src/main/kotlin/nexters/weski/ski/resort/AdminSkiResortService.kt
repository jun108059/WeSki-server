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

        request.name?.let { existingSkiResort.name = it }
        request.status?.let { existingSkiResort.status = it }
        request.openingDate?.let { existingSkiResort.openingDate = it }
        request.closingDate?.let { existingSkiResort.closingDate = it }
        request.dayOperatingHours?.let { existingSkiResort.dayOperatingHours = it }
        request.nightOperatingHours?.let { existingSkiResort.nightOperatingHours = it }
        request.lateNightOperatingHours?.let { existingSkiResort.lateNightOperatingHours = it }
        request.dawnOperatingHours?.let { existingSkiResort.dawnOperatingHours = it }
        request.midnightOperatingHours?.let { existingSkiResort.midnightOperatingHours = it }
        request.snowfallTime?.let { existingSkiResort.snowfallTime = it }
        request.xCoordinate?.let { existingSkiResort.xCoordinate = it }
        request.yCoordinate?.let { existingSkiResort.yCoordinate = it }
        request.detailedAreaCode?.let { existingSkiResort.detailedAreaCode = it }
        request.broadAreaCode?.let { existingSkiResort.broadAreaCode = it }

        val savedSkiResort = skiResortRepository.save(existingSkiResort)
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
                skiResort.status = newStatus
                skiResortRepository.save(skiResort)
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
                skiResort.totalSlopes = totalSlopeCount
                skiResort.openSlopes = openingSlopeCount
                skiResortRepository.save(skiResort)
            }
        }
    }
}
