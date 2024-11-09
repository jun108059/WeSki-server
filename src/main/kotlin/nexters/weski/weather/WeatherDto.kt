package nexters.weski.weather

import java.time.LocalDateTime

data class WeatherDto(
    val resortId: Long,
    val currentWeather: CurrentWeatherDto,
    val hourlyWeather: List<HourlyWeatherDto>,
    val weeklyWeather: List<DailyWeatherDto>
) {
    companion object {
        fun fromEntities(
            currentWeather: CurrentWeather,
            hourlyWeather: List<HourlyWeather>,
            dailyWeather: List<DailyWeather>
        ): WeatherDto {
            return WeatherDto(
                resortId = currentWeather.skiResort.resortId,
                currentWeather = CurrentWeatherDto.fromEntity(currentWeather),
                hourlyWeather = hourlyWeather.map { HourlyWeatherDto.fromEntity(it) },
                weeklyWeather = dailyWeather.map { DailyWeatherDto.fromEntity(it) }
            )
        }
    }
}

data class CurrentWeatherDto(
    val temperature: Int,
    val maxTemperature: Int,
    val minTemperature: Int,
    val feelsLike: Int,
    val description: String,
    val condition: String
) {
    companion object {
        fun fromEntity(entity: CurrentWeather): CurrentWeatherDto {
            return CurrentWeatherDto(
                temperature = entity.temperature,
                maxTemperature = entity.maxTemp,
                minTemperature = entity.minTemp,
                feelsLike = entity.feelsLike,
                description = entity.description,
                condition = entity.condition
            )
        }
    }
}

data class HourlyWeatherDto(
    val time: String,
    val temperature: Int,
    val precipitationChance: String,
    val condition: String
) {
    companion object {
        fun fromEntity(entity: HourlyWeather): HourlyWeatherDto {
            return HourlyWeatherDto(
                time = entity.forecastTime.toLocalTimeString(),
                temperature = entity.temperature,
                precipitationChance = entity.precipitationChance.toPercentString(),
                condition = entity.condition
            )
        }
    }
}

data class DailyWeatherDto(
    val day: String,
    val date: String,
    val precipitationChance: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val condition: String,
) {
    companion object {
        fun fromEntity(entity: DailyWeather): DailyWeatherDto {
            return DailyWeatherDto(
                day = entity.dayOfWeek,
                date = entity.forecastDate.toString().toShortDate(),
                precipitationChance = entity.precipitationChance.toPercentString(),
                maxTemperature = entity.maxTemp,
                minTemperature = entity.minTemp,
                condition = entity.condition,
            )
        }
    }
}

data class SimpleCurrentWeatherDto(
    val temperature: Int,
    val description: String,
) {
    companion object {
        fun fromEntity(entity: CurrentWeather): SimpleCurrentWeatherDto {
            return SimpleCurrentWeatherDto(
                temperature = entity.temperature,
                description = entity.description,
            )
        }
    }
}

data class WeeklyWeatherDto(
    val day: String,
    val maxTemperature: Int,
    val minTemperature: Int,
    val description: String,
) {
    companion object {
        fun fromEntity(entity: DailyWeather): WeeklyWeatherDto {
            return WeeklyWeatherDto(
                day = entity.dayOfWeek,
                maxTemperature = entity.maxTemp,
                minTemperature = entity.minTemp,
                description = entity.condition
            )
        }
    }
}

// 2024-08-01 형태에서 8.1 형태로 변경하는 메서드 호출
fun String.toShortDate(): String {
    val date = this.split("-")
    return "${date[1]}.${date[2]}"
}

// Int 데이터를 Int+% String으로 변환하는 메서드
fun Int.toPercentString(): String {
    return "$this%"
}

// LocalDateTime 데이터를 오전/오후 n시로 변경하는 메서드
fun LocalDateTime.toLocalTimeString(): String {
    val hour = this.hour
    return if (hour < 12) {
        "오전 ${hour}시"
    } else if (hour == 12) {
        "오후 ${hour}시"
    } else {
        "오후 ${hour - 12}시"
    }
}