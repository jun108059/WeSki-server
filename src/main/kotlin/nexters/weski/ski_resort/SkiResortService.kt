package nexters.weski.ski_resort

import nexters.weski.batch.DateType
import nexters.weski.slope.SlopeService
import nexters.weski.weather.CurrentWeatherRepository
import nexters.weski.weather.DailyWeatherRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SkiResortService(
    private val skiResortRepository: SkiResortRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository,
    private val slopeService: SlopeService
) {
    fun getAllSkiResortsAndWeather(): List<SkiResortResponseDto> {
        val skiResorts = skiResortRepository.findAllByOrderByOpeningDateAsc()
        return skiResorts.map { skiResort ->
            val currentWeather = currentWeatherRepository.findBySkiResortResortId(skiResort.resortId)
            val weeklyWeather = dailyWeatherRepository.findBySkiResortAndForecastDateBetweenOrderByForecastDate(
                skiResort = skiResort,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(7)
            )

            SkiResortResponseDto.fromEntity(skiResort, currentWeather, weeklyWeather)
        }
    }

    fun getSkiResortAndWeather(resortId: Long): SkiResortResponseDto {
        val skiResort = skiResortRepository.findById(resortId)
            .orElseThrow { IllegalArgumentException("해당 ID의 스키장이 존재하지 않습니다.") }

        val currentWeather = currentWeatherRepository.findBySkiResortResortId(skiResort.resortId)
        val weeklyWeather = dailyWeatherRepository.findBySkiResortAndForecastDateBetweenOrderByForecastDate(
            skiResort = skiResort,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7)
        )

        return SkiResortResponseDto.fromEntity(skiResort, currentWeather, weeklyWeather)
    }

    fun updateResortDate(resortId: Long, dateType: DateType, date: LocalDate) {
        val skiResort = skiResortRepository.findById(resortId)
            .orElseThrow { IllegalArgumentException("해당 ID의 스키장이 존재하지 않습니다.") }

        val updatedSkiResort = when (dateType) {
            DateType.OPENING_DATE -> skiResort.copy(openingDate = date)
            DateType.CLOSING_DATE -> skiResort.copy(closingDate = date)
        }

        skiResortRepository.save(updatedSkiResort)
    }

    fun updateSkiResortStatus() {
        val skiResorts = skiResortRepository.findAll()
        val today = LocalDate.now()

        skiResorts.forEach { skiResort ->
            val openingDate = skiResort.openingDate
            val closingDate = skiResort.closingDate

            val newStatus = when {
                today.isBefore(openingDate) -> ResortStatus.예정
                closingDate != null && today.isAfter(closingDate) -> ResortStatus.운영종료
                else -> ResortStatus.운영중
            }

            val updatedSkiResort = skiResort.copy(status = newStatus)
            skiResortRepository.save(updatedSkiResort)
        }
    }

    fun updateSkiResortSlopeCount() {
        val skiResorts = skiResortRepository.findAll()
        skiResorts.forEach { skiResort ->
            val totalSlopeCount = slopeService.getTotalSlopeCount(skiResort.resortId)
            val openingSlopeCount = slopeService.getOpeningSlopeCount(skiResort.resortId)
            val updatedSkiResort = skiResort.copy(totalSlopes = totalSlopeCount, openSlopes = openingSlopeCount)
            skiResortRepository.save(updatedSkiResort)
        }
    }
}
