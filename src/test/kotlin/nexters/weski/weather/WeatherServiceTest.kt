package nexters.weski.weather

import io.mockk.every
import io.mockk.mockk
import nexters.weski.ski.resort.ResortStatus
import nexters.weski.ski.resort.SkiResort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class WeatherServiceTest {
    private val currentWeatherRepository: CurrentWeatherRepository = mockk()
    private val hourlyWeatherRepository: HourlyWeatherRepository = mockk()
    private val dailyWeatherRepository: DailyWeatherRepository = mockk()
    private val weatherService =
        WeatherService(currentWeatherRepository, hourlyWeatherRepository, dailyWeatherRepository)

    @Test
    fun `getWeatherByResortId should return WeatherDto`() {
        // Given
        val resortId = 1L
        val skiResort =
            SkiResort(
                resortId = resortId,
                name = "스키장 A",
                status = ResortStatus.운영중,
                openingDate = null,
                closingDate = null,
                openSlopes = 5,
                totalSlopes = 10,
                dayOperatingHours = "09:00 ~ 17:00",
                nightOperatingHours = "18:00 ~ 22:00",
                lateNightOperatingHours = "22:00 ~ 24:00",
                dawnOperatingHours = "06:00 ~ 09:00",
                midnightOperatingHours = "00:00 ~ 02:00",
                snowfallTime = "눈이 내리고 있습니다.",
                xCoordinate = "37.123456",
                yCoordinate = "127.123456",
                xRealCoordinate = 123456.0,
                yRealCoordinate = 123456.0,
                detailedAreaCode = "Test",
                broadAreaCode = "Test",
            )
        val currentWeather =
            CurrentWeather(
                resortId,
                -5,
                -2,
                -8,
                -10,
                "눈이 내리고 있습니다.",
                "눈",
                skiResort,
            )
        every { currentWeatherRepository.findBySkiResortResortId(resortId) } returns currentWeather
        every { hourlyWeatherRepository.findBySkiResortResortId(resortId) } returns listOf()
        every {
            dailyWeatherRepository.findAllBySkiResortResortIdAndForecastDateBetweenOrderByForecastDate(
                resortId,
                any(),
                any(),
            )
        } returns listOf()

        // When
        val result = weatherService.getWeatherByResortId(resortId)

        // Then
        assertNotNull(result)
        assertEquals(-5, result?.currentWeather?.temperature)
    }
}
