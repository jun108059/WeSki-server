package nexters.weski.ski_resort

import nexters.weski.weather.CurrentWeather
import nexters.weski.weather.DailyWeather
import nexters.weski.weather.SimpleCurrentWeatherDto
import nexters.weski.weather.WeeklyWeatherDto

data class SkiResortResponseDto(
    val resortId: Long,
    val name: String,
    val status: String,
    val openSlopes: Int,
    val currentWeather: SimpleCurrentWeatherDto,
    val weeklyWeather: List<WeeklyWeatherDto>
) {
    companion object {
        fun fromEntity(
            skiResort: SkiResort,
            currentWeather: CurrentWeather?,
            weeklyWeather: List<DailyWeather>
        ): SkiResortResponseDto {
            return SkiResortResponseDto(
                resortId = skiResort.resortId,
                name = skiResort.name,
                status = skiResort.status.name,
                openSlopes = skiResort.openSlopes,
                currentWeather = currentWeather?.let {
                    SimpleCurrentWeatherDto(
                        temperature = it.temperature,
                        description = it.condition
                    )
                } ?: SimpleCurrentWeatherDto(0, "정보 없음"),
                weeklyWeather = weeklyWeather.map {
                    WeeklyWeatherDto(
                        day = it.dayOfWeek,
                        maxTemperature = it.maxTemp,
                        minTemperature = it.minTemp,
                        description = it.condition
                    )
                }
            )
        }
    }
}