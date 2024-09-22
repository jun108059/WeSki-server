package nexters.weski.ski_resort

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ski-resorts")
class SkiResortController(
    private val skiResortService: SkiResortService
) {
    @GetMapping
    fun getAllSkiResorts(): List<SkiResortDto> {
        return skiResortService.getAllSkiResorts()
    }
}
