package nexters.weski.slope

import org.springframework.data.jpa.repository.JpaRepository

interface SlopeRepository : JpaRepository<Slope, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<Slope>
}
