package nexters.weski.snowmaker

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nexters.weski.ski.resort.ResortStatus
import nexters.weski.ski.resort.SkiResort
import nexters.weski.ski.resort.SkiResortRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Optional

class SnowMakerServiceTest {
    private val snowMakerVoteRepository: SnowMakerVoteRepository = mockk(relaxed = true)
    private val skiResortRepository: SkiResortRepository = mockk()
    private val snowMakerService = SnowMakerService(snowMakerVoteRepository, skiResortRepository)

    @Test
    fun `getSnowMaker should return SnowMakerDto`() {
        // Given
        val resortId = 1L
        every { snowMakerVoteRepository.countBySkiResortResortId(resortId) } returns 100
        every { snowMakerVoteRepository.countBySkiResortResortIdAndIsPositive(resortId, true) } returns 80

        // When
        val result = snowMakerService.getSnowMaker(resortId)

        // Then
        assertEquals(100, result.totalVotes)
        assertEquals(80, result.positiveVotes)
        assertEquals("좋음", result.status)
    }

    @Test
    fun `voteSnowMaker should save vote`() {
        // Given
        val resortId = 1L
        val isPositive = true
        val skiResort =
            SkiResort(
                resortId = 1,
                name = "스키장 A",
                status = ResortStatus.운영중,
                openingDate = null,
                closingDate = null,
                openSlopes = 3,
                totalSlopes = 8,
                xCoordinate = "12.0",
                yCoordinate = "34.0",
                detailedAreaCode = "11D20201",
                broadAreaCode = "11D20000",
            )
        val snowMakerVote =
            SnowMakerVote(
                isPositive = isPositive,
                skiResort = skiResort,
            )
        every { skiResortRepository.findById(resortId) } returns Optional.of(skiResort)
        every { snowMakerVoteRepository.save(any()) } returns snowMakerVote

        // When
        snowMakerService.voteSnowMaker(resortId, isPositive)

        // Then
        verify { snowMakerVoteRepository.save(any()) }
    }
}
