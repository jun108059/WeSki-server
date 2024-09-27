package nexters.weski.ski_resort

import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import com.ninjasquad.springmockk.MockkBean

@WebMvcTest(SkiResortController::class)
class SkiResortControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    lateinit var skiResortService: SkiResortService

    @Test
    fun `GET api_ski-resorts should return list of ski resorts`() {
        // Given
        val skiResorts = listOf(
            SkiResortDto(1, "스키장 A", ResortStatus.운영중, "2023-12-01", "2024-03-01", 5, 10),
            SkiResortDto(2, "스키장 B", ResortStatus.예정, "2023-12-15", null, 0, 8)
        )
        every { skiResortService.getAllSkiResorts() } returns skiResorts

        // When & Then
        mockMvc.perform(get("/api/ski-resorts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("스키장 A"))
            .andExpect(jsonPath("$[1].name").value("스키장 B"))
    }
}