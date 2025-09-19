package nexters.weski.slope

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SlopeRepository : JpaRepository<Slope, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<Slope>

    fun countBySkiResortResortId(resortId: Long): Int

    fun findBySkiResortResortIdAndName(
        resortId: Long,
        name: String,
    ): Slope?

    @Query(
        """
        SELECT COUNT(s)
        FROM Slope s
        WHERE s.skiResort.resortId = :resortId 
          AND (s.isDayOperating = true 
               OR s.isNightOperating = true 
               OR s.isLateNightOperating = true 
               OR s.isDawnOperating = true 
               OR s.isMidnightOperating = true)
    """,
    )
    fun countOperatingSlopesByResortId(resortId: Long): Int
}
