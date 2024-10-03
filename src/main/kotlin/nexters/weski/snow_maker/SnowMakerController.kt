package nexters.weski.snow_maker

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/snow-maker")

class SnowMakerController(
    private val snowMakerService: SnowMakerService
) {
    @GetMapping("/{resortId}")
    fun getSnowMaker(@PathVariable resortId: Long): SnowMakerDto {
        return snowMakerService.getSnowMaker(resortId)
    }

    @PostMapping("/{resortId}/vote")
    fun voteSnowMaker(@PathVariable resortId: Long, @RequestParam isPositive: Boolean) {
        snowMakerService.voteSnowMaker(resortId, isPositive)
    }
}
