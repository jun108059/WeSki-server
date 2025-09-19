package nexters.weski.slope

import nexters.weski.ski.resort.SkiResort
import nexters.weski.webcam.Webcam
import nexters.weski.webcam.WebcamDto

data class SlopeResponseDto(
    val dayOperatingHours: String?,
    val nightOperatingHours: String?,
    val lateNightOperatingHours: String?,
    val dawnOperatingHours: String?,
    val midnightOperatingHours: String?,
    val slopes: List<SlopeDto>,
    val webcams: List<WebcamDto>,
) {
    companion object {
        fun fromEntities(
            skiResort: SkiResort,
            slopes: List<Slope>,
            webcams: List<Webcam>,
        ): SlopeResponseDto =
            SlopeResponseDto(
                dayOperatingHours = skiResort.dayOperatingHours,
                nightOperatingHours = skiResort.nightOperatingHours,
                lateNightOperatingHours = skiResort.lateNightOperatingHours,
                dawnOperatingHours = skiResort.dawnOperatingHours,
                midnightOperatingHours = skiResort.midnightOperatingHours,
                slopes = slopes.map { SlopeDto.fromEntity(it) },
                webcams = webcams.map { WebcamDto.fromEntity(it) },
            )
    }
}
