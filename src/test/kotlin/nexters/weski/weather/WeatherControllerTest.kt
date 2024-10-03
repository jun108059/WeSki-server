package nexters.weski.weather

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import nexters.weski.common.config.JpaConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(WeatherController::class)
@ComponentScan(
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [JpaConfig::class, WeatherUpdateService::class]
    )]
)
class WeatherControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    lateinit var weatherService: WeatherService

    @Test
    fun `GET api_weather_resortId should return weather data`() {
        // Given
        val resortId = 1L
        val weatherDto = WeatherDto(
            resortId,
            CurrentWeatherDto(-5, -2, -8, -10, "눈이 내리고 있습니다.", "눈"),
            listOf(),
            listOf()
        )
        every { weatherService.getWeatherByResortId(resortId) } returns weatherDto

        // When & Then
        mockMvc.perform(get("/api/weather/$resortId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.currentWeather.temperature").value(-5))
            .andExpect(jsonPath("$.currentWeather.condition").value("눈"))
    }
}