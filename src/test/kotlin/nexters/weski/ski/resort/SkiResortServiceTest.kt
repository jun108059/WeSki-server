package nexters.weski.ski.resort

import io.mockk.every
import io.mockk.mockk
import nexters.weski.weather.CurrentWeatherRepository
import nexters.weski.weather.DailyWeatherRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SkiResortServiceTest {

    private val skiResortRepository: SkiResortRepository = mockk()
    private val currentWeatherRepository: CurrentWeatherRepository = mockk()
    private val dailyWeatherRepository: DailyWeatherRepository = mockk()
    private val skiResortService = SkiResortService(
        skiResortRepository,
        currentWeatherRepository,
        dailyWeatherRepository,
    )

    @Test
    fun `getAllSkiResorts should return list of SkiResortDto`() {
        // Given
        val skiResorts = listOf(
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
                broadAreaCode = "11D20000"
            ),
            SkiResort(
                resortId = 2,
                name = "스키장 B",
                status = ResortStatus.운영중,
                openingDate = null,
                closingDate = null,
                openSlopes = 3,
                totalSlopes = 8,
                xCoordinate = "12.0",
                yCoordinate = "34.0",
                detailedAreaCode = "11D20201",
                broadAreaCode = "11D20000"
            )
        )
        every { skiResortRepository.findAllByOrderByOpeningDateAsc() } returns skiResorts
        every { currentWeatherRepository.findBySkiResortResortId(any()) } returns null
        every { dailyWeatherRepository.findAllBySkiResortResortId(any()) } returns emptyList()

        // When
        val result = skiResortService.getAllSkiResortsAndWeather()

        // Then
        assertEquals(2, result.size)
        assertEquals("스키장 A", result[0].name)
        assertEquals("스키장 B", result[1].name)
    }
}