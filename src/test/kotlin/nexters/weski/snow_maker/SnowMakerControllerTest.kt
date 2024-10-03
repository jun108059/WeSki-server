package nexters.weski.snow_maker


import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import nexters.weski.common.config.JpaConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(SnowMakerController::class)
@ComponentScan(
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [JpaConfig::class]
    )]
)
class SnowMakerControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    lateinit var snowMakerService: SnowMakerService

    @Test
    fun `GET api_snow-maker_resortId should return snow Maker data`() {
        // Given
        val resortId = 1L
        val snowMakerDto = SnowMakerDto(resortId, 100, 80, "좋음")
        every { snowMakerService.getSnowMaker(resortId) } returns snowMakerDto

        // When & Then
        mockMvc.perform(get("/api/snow-maker/$resortId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalVotes").value(100))
            .andExpect(jsonPath("$.status").value("좋음"))
    }

    @Test
    fun `POST api_snow-maker_resortId_vote should vote successfully`() {
        // Given
        val resortId = 1L
        every { snowMakerService.voteSnowMaker(any(), any()) } just Runs

        // When & Then
        mockMvc.perform(post("/api/snow-maker/$resortId/vote")
            .param("isPositive", "true"))
            .andExpect(status().isOk)
    }
}
