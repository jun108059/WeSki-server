package nexters.weski.ski_resort

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SkiResortServiceTest {

    private val skiResortRepository: SkiResortRepository = mockk()
    private val skiResortService = SkiResortService(skiResortRepository)

    @Test
    fun `getAllSkiResorts should return list of SkiResortDto`() {
        // Given
        val skiResorts = listOf(
            SkiResort(1, "스키장 A", ResortStatus.운영중, null, null, 5, 10),
            SkiResort(2, "스키장 B", ResortStatus.예정, null, null, 0, 8)
        )
        every { skiResortRepository.findAll() } returns skiResorts

        // When
        val result = skiResortService.getAllSkiResorts()

        // Then
        assertEquals(2, result.size)
        assertEquals("스키장 A", result[0].name)
        assertEquals("스키장 B", result[1].name)
    }
}