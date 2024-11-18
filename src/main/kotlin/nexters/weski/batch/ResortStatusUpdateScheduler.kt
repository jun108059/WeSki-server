package nexters.weski.batch

import nexters.weski.ski_resort.SkiResortService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ResortStatusUpdateScheduler(
    private val skiResortService: SkiResortService
) {
    @Scheduled(cron = "15 0 0 * * ?")
    fun scheduleResortStatusUpdate() {
        skiResortService.updateSkiResortStatus()
    }
}