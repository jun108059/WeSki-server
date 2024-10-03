package nexters.weski.slope

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/slopes")
class SlopeController(
    private val slopeService: SlopeService
) {
    @GetMapping("/{resortId}")
    fun getSlopesAndWebcams(@PathVariable resortId: Long): SlopeResponseDto {
        return slopeService.getSlopesAndWebcams(resortId)
    }
}
