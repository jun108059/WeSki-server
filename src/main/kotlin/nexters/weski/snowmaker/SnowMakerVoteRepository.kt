package nexters.weski.snowmaker

import org.springframework.data.jpa.repository.JpaRepository

interface SnowMakerVoteRepository : JpaRepository<SnowMakerVote, Long> {
    fun countBySkiResortResortId(resortId: Long): Long

    fun countBySkiResortResortIdAndIsPositive(
        resortId: Long,
        isPositive: Boolean,
    ): Long
}
