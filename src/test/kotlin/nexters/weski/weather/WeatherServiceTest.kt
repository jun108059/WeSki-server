package nexters.weski.weather

import io.mockk.every
import io.mockk.mockk
import nexters.weski.ski_resort.ResortStatus
import nexters.weski.ski_resort.SkiResort
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
        val skiResort = SkiResort(
            resortId, "스키장 A", ResortStatus.운영중, null, null, 5, 10,
            "09:00 ~ 17:00", "18:00 ~ 22:00", "22:00 ~ 24:00", "06:00 ~ 09:00", "00:00 ~ 02:00",
            "눈이 내리고 있습니다.", "37.123456", "127.123456", "123456", "123456"
        )
        val currentWeather = CurrentWeather(
            resortId, -5, -2, -8, -10, "눈이 내리고 있습니다.", "눈", skiResort
        )
        every { currentWeatherRepository.findBySkiResortResortId(resortId) } returns currentWeather
        every { hourlyWeatherRepository.findAll() } returns listOf()
        every { dailyWeatherRepository.findAllBySkiResortResortId(resortId) } returns listOf()

        // When
        val result = weatherService.getWeatherByResortId(resortId)

        // Then
        assertNotNull(result)
        assertEquals(-5, result?.currentWeather?.temperature)
    }
}