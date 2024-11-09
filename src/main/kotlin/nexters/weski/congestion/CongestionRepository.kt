package nexters.weski.congestion

import org.springframework.data.jpa.repository.JpaRepository

interface CongestionRepository : JpaRepository<Congestion, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<Congestion>
}
