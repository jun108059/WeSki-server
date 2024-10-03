package nexters.weski.snow_maker

import nexters.weski.ski_resort.SkiResortRepository
import org.springframework.stereotype.Service

@Service
class SnowMakerService(
    private val snowMakerVoteRepository: SnowMakerVoteRepository,
    private val skiResortRepository: SkiResortRepository
) {
    fun getSnowMaker(resortId: Long): SnowMakerDto {
        val totalVotes = snowMakerVoteRepository.countBySkiResortResortId(resortId)
        val positiveVotes = snowMakerVoteRepository.countBySkiResortResortIdAndIsPositive(resortId, true)
        val status = calculateStatus(totalVotes, positiveVotes)

        return SnowMakerDto(
            resortId = resortId,
            totalVotes = totalVotes,
            positiveVotes = positiveVotes,
            status = status
        )
    }

    fun voteSnowMaker(resortId: Long, isPositive: Boolean) {
        val skiResort = skiResortRepository.findById(resortId).orElseThrow { Exception("Resort not found") }
        val vote = SnowMakerVote(
            isPositive = isPositive,
            skiResort = skiResort
        )
        snowMakerVoteRepository.save(vote)
    }

    private fun calculateStatus(totalVotes: Long, positiveVotes: Long): String {
        if (totalVotes == 0L) return "정보 없음"
        val positiveRate = (positiveVotes.toDouble() / totalVotes.toDouble()) * 100
        return when {
            positiveRate >= 80 -> "좋음"
            positiveRate >= 50 -> "나쁘지 않음"
            else -> "좋지 않음"
        }
    }
}