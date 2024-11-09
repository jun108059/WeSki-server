package nexters.weski.batch

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import nexters.weski.ski_resort.SkiResort
import nexters.weski.ski_resort.SkiResortRepository
import nexters.weski.weather.CurrentWeather
import nexters.weski.weather.CurrentWeatherRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow

@Service
class ExternalWeatherService(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val skiResortRepository: SkiResortRepository
) {
    @Value("\${weather.api.key}")
    lateinit var apiKey: String

    val restTemplate = RestTemplate()
    val objectMapper = jacksonObjectMapper()

    @Transactional
    fun updateCurrentWeather() {
        val baseDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val baseTime = getBaseTime()
        skiResortRepository.findAll().forEach { resort ->
            val url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" +
                    "?serviceKey=$apiKey" +
                    "&pageNo=1" +
                    "&numOfRows=1000" +
                    "&dataType=JSON" +
                    "&base_date=$baseDate" +
                    "&base_time=$baseTime" +
                    "&nx=${resort.xCoordinate}" +
                    "&ny=${resort.yCoordinate}"
            val response = restTemplate.getForObject(url, String::class.java)
            val weatherData = parseWeatherData(response)
            val newCurrentWeather = mapToCurrentWeather(weatherData, resort)

            // 기존 데이터 조회
            val existingWeather = currentWeatherRepository.findBySkiResortResortId(resort.resortId)

            if (existingWeather != null) {
                // 기존 데이터의 ID를 사용하여 새로운 엔티티 생성
                val updatedWeather = newCurrentWeather.copy(id = existingWeather.id)
                currentWeatherRepository.save(updatedWeather)
            } else {
                // 새로운 데이터 삽입
                currentWeatherRepository.save(newCurrentWeather)
            }
        }
    }

    private fun getBaseTime(): String {
        val now = LocalDateTime.now().minusHours(1)
        val hour = now.hour.toString().padStart(2, '0')
        return "${hour}00"
    }

    private fun parseWeatherData(response: String?): Map<String, String> {
        val data = mutableMapOf<String, String>()

        response?.let {
            val rootNode = objectMapper.readTree(it)
            val items = rootNode["response"]["body"]["items"]["item"]

            items.forEach { item ->
                val category = item["category"].asText()
                val value = item["obsrValue"].asText()
                data[category] = value
            }
        }

        return data
    }

    private fun mapToCurrentWeather(
        data: Map<String, String>,
        resort: SkiResort
    ): CurrentWeather {
        val temperature = data["T1H"]?.toDoubleOrNull()?.toInt() ?: 0
        val windSpeed = data["WSD"]?.toDoubleOrNull() ?: 0.0
        val feelsLike = calculateFeelsLike(temperature, windSpeed)
        val condition = determineCondition(data)
        val description = generateDescription(condition, temperature)

        return CurrentWeather(
            temperature = temperature,
            maxTemp = data["TMX"]?.toDoubleOrNull()?.toInt() ?: temperature,
            minTemp = data["TMN"]?.toDoubleOrNull()?.toInt() ?: temperature,
            feelsLike = feelsLike,
            condition = condition,
            description = description,
            skiResort = resort
        )
    }

    private fun calculateFeelsLike(temperature: Int, windSpeed: Double): Int {
        return if (temperature <= 10 && windSpeed >= 4.8) {
            val feelsLike =
                13.12 + 0.6215 * temperature - 11.37 * windSpeed.pow(0.16) + 0.3965 * temperature * windSpeed.pow(
                    0.16
                )
            feelsLike.toInt()
        } else {
            temperature
        }
    }

    private fun determineCondition(data: Map<String, String>): String {
        val pty = data["PTY"]?.toIntOrNull() ?: 0
        val sky = data["SKY"]?.toIntOrNull() ?: 1

        return when {
            pty == 1 || pty == 4 -> "비"
            pty == 2 -> "비/눈"
            pty == 3 -> "눈"
            sky == 1 -> "맑음"
            sky == 3 -> "구름많음"
            sky == 4 -> "흐림"
            else -> "맑음"
        }
    }

    private fun generateDescription(condition: String, temperature: Int): String {
        val prefix = when (condition) {
            "맑음" -> "화창하고"
            "구름많음" -> "구름이 많고"
            "흐림" -> "흐리고"
            "비" -> "비가 오고"
            "비/눈" -> "눈비가 내리고"
            "눈" -> "눈이 오고"
            else -> ""
        }

        val postfix = when {
            temperature <= -15 -> "매우 추워요"
            temperature in -14..-10 -> "다소 추워요"
            temperature in -9..-5 -> "적당한 온도에요"
            temperature in -4..0 -> "조금 따뜻해요"
            temperature in 1..5 -> "따뜻해요"
            temperature in 6..10 -> "다소 더워요"
            else -> "더워요"
        }

        return "$prefix $postfix"
    }
}