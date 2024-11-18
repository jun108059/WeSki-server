package nexters.weski.ski_resort

import nexters.weski.batch.DateType
import nexters.weski.weather.CurrentWeatherRepository
import nexters.weski.weather.DailyWeatherRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SkiResortService(
    private val skiResortRepository: SkiResortRepository,
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository
) {
    fun getAllSkiResortsAndWeather(): List<SkiResortResponseDto> {
        val skiResorts = skiResortRepository.findAllByOrderByOpeningDateAsc()
        return skiResorts.map { skiResort ->
            val currentWeather = currentWeatherRepository.findBySkiResortResortId(skiResort.resortId)
            val weeklyWeather = dailyWeatherRepository.findAllBySkiResortResortId(skiResort.resortId)

            SkiResortResponseDto.fromEntity(skiResort, currentWeather, weeklyWeather)
        }
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

}
