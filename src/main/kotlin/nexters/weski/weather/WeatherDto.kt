package nexters.weski.weather

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
                resortId = currentWeather.resortId,
                currentWeather = CurrentWeatherDto.fromEntity(currentWeather),
                hourlyWeather = hourlyWeather.map { HourlyWeatherDto.fromEntity(it) },
                weeklyWeather = dailyWeather.map { DailyWeatherDto.fromEntity(it) }
            )
        }
    }
}

data class CurrentWeatherDto(
    val temperature: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val feelsLike: Int,
    val description: String,
    val condition: String
) {
    companion object {
        fun fromEntity(entity: CurrentWeather): CurrentWeatherDto {
            return CurrentWeatherDto(
                temperature = entity.temperature,
                maxTemp = entity.maxTemp,
                minTemp = entity.minTemp,
                feelsLike = entity.feelsLike,
                description = entity.description,
                condition = entity.condition.name
            )
        }
    }
}

data class HourlyWeatherDto(
    val time: String,
    val temperature: Int,
    val precipitationChance: Int,
    val condition: String
) {
    companion object {
        fun fromEntity(entity: HourlyWeather): HourlyWeatherDto {
            return HourlyWeatherDto(
                time = entity.forecastTime.toLocalTime().toString(),
                temperature = entity.temperature,
                precipitationChance = entity.precipitationChance,
                condition = entity.condition.name
            )
        }
    }
}

data class DailyWeatherDto(
    val day: String,
    val date: String,
    val precipitationChance: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val dayCondition: String,
    val nightCondition: String
) {
    companion object {
        fun fromEntity(entity: DailyWeather): DailyWeatherDto {
            return DailyWeatherDto(
                day = entity.dayOfWeek,
                date = entity.forecastDate.toString(),
                precipitationChance = entity.precipitationChance,
                maxTemp = entity.maxTemp,
                minTemp = entity.minTemp,
                dayCondition = entity.dayCondition.name,
                nightCondition = entity.nightCondition.name
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
                day = TODO(),
                maxTemperature = entity.maxTemp,
                minTemperature = entity.minTemp,
                description = entity.dayCondition.name
            )
        }
    }
}