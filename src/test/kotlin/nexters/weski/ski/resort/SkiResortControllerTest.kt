package nexters.weski.ski.resort

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import nexters.weski.common.config.JpaConfig
import nexters.weski.weather.SimpleCurrentWeatherDto
import nexters.weski.weather.WeeklyWeatherDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [SkiResortController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [AdminSkiResortController::class, JpaConfig::class],
        ),
    ],
)
class SkiResortControllerTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
    ) {
        @MockkBean
        lateinit var skiResortService: SkiResortService

        @Test
        fun `GET api_ski-resorts should return list of ski resorts`() {
            val currentWeather = SimpleCurrentWeatherDto(-1, "맑음")
            val weeklyWeather =
                listOf(
                    WeeklyWeatherDto("월", 5, -3, "맑음"),
                    WeeklyWeatherDto("화", 6, -2, "맑음"),
                    WeeklyWeatherDto("수", 7, -1, "맑음"),
                    WeeklyWeatherDto("목", 8, 0, "맑음"),
                    WeeklyWeatherDto("금", 9, 1, "맑음"),
                    WeeklyWeatherDto("토", 10, 2, "맑음"),
                    WeeklyWeatherDto("일", 11, 3, "맑음"),
                )
            // Given
            val skiResorts =
                listOf(
                    SkiResortResponseDto(1, "스키장 A", ResortStatus.운영중.name, "미정", "미정", 3, currentWeather, weeklyWeather),
                    SkiResortResponseDto(2, "스키장 B", ResortStatus.운영중.name, "미정", "미정", 4, currentWeather, weeklyWeather),
                )
            every { skiResortService.getAllSkiResortsAndWeather() } returns skiResorts

            // When & Then
            mockMvc
                .perform(get("/api/ski-resorts"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].name").value("스키장 A"))
                .andExpect(jsonPath("$[1].name").value("스키장 B"))
        }
    }
