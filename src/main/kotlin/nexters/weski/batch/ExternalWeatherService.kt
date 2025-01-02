package nexters.weski.batch

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import nexters.weski.ski_resort.SkiResort
import nexters.weski.ski_resort.SkiResortRepository
import nexters.weski.weather.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow

@Service
class ExternalWeatherService(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository,
    private val hourlyWeatherRepository: HourlyWeatherRepository,
    private val skiResortRepository: SkiResortRepository
) {
    @Value("\${weather.api.key}")
    lateinit var apiKey: String

    val restTemplate = RestTemplate()
    val objectMapper = jacksonObjectMapper()

    @Transactional
    fun updateCurrentWeather() {
        val baseTime = getBaseTime()
        val baseLocalDateTime = if (baseTime == "2300") {
            LocalDateTime.now().minusDays(1)
        } else {
            LocalDateTime.now()
        }
        val baseDate = baseLocalDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
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
        if (hour == "23") {
            return "0000"
        }
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
        val dailyWeather = dailyWeatherRepository.findBySkiResortAndForecastDate(resort, LocalDate.now())[0]
        // dailyWeather.maxTemp보다 temperature이 높으면 maxTemp를 업데이트
        if (temperature > dailyWeather.maxTemp) {
            dailyWeather.maxTemp = temperature
            dailyWeatherRepository.save(dailyWeather)
        }
        // dailyWeather.minTemp보다 temperature이 낮으면 minTemp을 업데이트
        if (temperature < dailyWeather.minTemp) {
            dailyWeather.minTemp = temperature
            dailyWeatherRepository.save(dailyWeather)
        }
        return CurrentWeather(
            temperature = temperature,
            maxTemp = dailyWeather.maxTemp,
            minTemp = dailyWeather.minTemp,
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

    @Transactional
    fun updateDailyWeather() {
        val baseDateTime = getYesterdayBaseDateTime()
        val baseDate = baseDateTime.first
        val baseTime = baseDateTime.second

        val tmFc = baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + baseTime
        // 기존 데이터 삭제
        dailyWeatherRepository.deleteByDDayGreaterThanEqual(4)
        skiResortRepository.findAll().forEach { resort ->
            val detailedAreaCode = resort.detailedAreaCode
            val broadAreaCode = resort.broadAreaCode

            // 첫 번째 API 호출 (중기 기온 예보)
            val midTaUrl = buildMidTaUrl(detailedAreaCode, tmFc)
            val midTaResponse = restTemplate.getForObject(midTaUrl, String::class.java)
            val midTaData = parseMidTaResponse(midTaResponse)

            // 두 번째 API 호출 (중기 육상 예보)
            val midLandUrl = buildMidLandUrl(broadAreaCode, tmFc)
            val midLandResponse = restTemplate.getForObject(midLandUrl, String::class.java)
            val midLandData = parseMidLandResponse(midLandResponse)

            // 데이터 병합 및 처리
            val dailyWeathers = mergeWeatherData(resort, midTaData, midLandData)
            dailyWeatherRepository.saveAll(dailyWeathers)
        }
    }

    private fun getYesterdayBaseDateTime(): Pair<LocalDate, String> {
        // 어제 날짜
        val yesterday = LocalDate.now().minusDays(1)
        val hour = 18 // 18시 기준
        return Pair(yesterday, String.format("%02d00", hour))
    }

    private fun buildMidTaUrl(areaCode: String, tmFc: String): String {
        return "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" +
                "?serviceKey=$apiKey" +
                "&pageNo=1" +
                "&numOfRows=10" +
                "&dataType=JSON" +
                "&regId=$areaCode" +
                "&tmFc=$tmFc"
    }

    private fun buildMidLandUrl(regId: String, tmFc: String): String {
        return "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst" +
                "?serviceKey=$apiKey" +
                "&pageNo=1" +
                "&numOfRows=10" +
                "&dataType=JSON" +
                "&regId=$regId" +
                "&tmFc=$tmFc"
    }

    private fun parseMidTaResponse(response: String?): JsonNode? {
        response ?: return null
        val rootNode = objectMapper.readTree(response)
        return rootNode["response"]["body"]["items"]["item"]?.get(0)
    }

    private fun parseMidLandResponse(response: String?): JsonNode? {
        response ?: return null
        val rootNode = objectMapper.readTree(response)
        return rootNode["response"]["body"]["items"]["item"]?.get(0)
    }

    private fun mergeWeatherData(
        resort: SkiResort,
        midTaData: JsonNode?,
        midLandData: JsonNode?
    ): List<DailyWeather> {
        val weatherList = mutableListOf<DailyWeather>()
        val now = LocalDate.now()

        if (midTaData == null || midLandData == null) {
            return weatherList
        }

        for (i in 5..10) {
            val forecastDate = now.plusDays(i.toLong() - 1)
            val dayOfWeek = forecastDate.dayOfWeek.name // 영어 요일명

            val maxTemp = midTaData.get("taMax$i")?.asInt() ?: continue
            val minTemp = midTaData.get("taMin$i")?.asInt() ?: continue

            val precipitationChance = getPrecipitationChance(midLandData, i)
            val condition = getCondition(midLandData, i)

            val dailyWeather = DailyWeather(
                skiResort = resort,
                forecastDate = forecastDate,
                dayOfWeek = convertDayOfWeek(dayOfWeek),
                dDay = i - 1,
                precipitationChance = precipitationChance,
                maxTemp = maxTemp,
                minTemp = minTemp,
                condition = condition
            )
            weatherList.add(dailyWeather)
        }

        return weatherList
    }

    private fun getPrecipitationChance(midLandData: JsonNode, day: Int): Int {
        return when (day) {
            in 5..7 -> {
                val amChance = midLandData.get("rnSt${day}Am")?.asInt() ?: 0
                val pmChance = midLandData.get("rnSt${day}Pm")?.asInt() ?: 0
                maxOf(amChance, pmChance)
            }

            in 8..10 -> {
                midLandData.get("rnSt$day")?.asInt() ?: 0
            }

            else -> 0
        }
    }

    private fun getCondition(midLandData: JsonNode, day: Int): String {
        return when (day) {
            in 5..7 -> {
                val amCondition = midLandData.get("wf${day}Am")?.asText() ?: ""
                val pmCondition = midLandData.get("wf${day}Pm")?.asText() ?: ""
                selectWorseCondition(amCondition, pmCondition)
            }

            in 8..10 -> {
                midLandData.get("wf$day")?.asText() ?: ""
            }

            else -> "알 수 없음"
        }
    }

    private fun selectWorseCondition(am: String, pm: String): String {
        val conditionPriority = listOf(
            "맑음",
            "구름많음",
            "흐림",
            "구름많고 소나기",
            "구름많고 비",
            "구름많고 비/눈",
            "흐리고 비",
            "흐리고 소나기",
            "소나기",
            "비",
            "비/눈",
            "흐리고 눈",
            "흐리고 비/눈",
            "눈"
        )
        val amIndex = conditionPriority.indexOf(am)
        val pmIndex = conditionPriority.indexOf(pm)

        return if (amIndex == -1 && pmIndex == -1) {
            "맑음"
        } else if (amIndex == -1) {
            pm
        } else if (pmIndex == -1) {
            am
        } else {
            if (amIndex > pmIndex) am else pm
        }
    }

    private fun convertDayOfWeek(englishDay: String): String {
        return when (englishDay) {
            "MONDAY" -> "월요일"
            "TUESDAY" -> "화요일"
            "WEDNESDAY" -> "수요일"
            "THURSDAY" -> "목요일"
            "FRIDAY" -> "금요일"
            "SATURDAY" -> "토요일"
            "SUNDAY" -> "일요일"
            else -> "ERROR"
        }
    }

    @Transactional
    fun updateHourlyAndDailyWeather() {
        val baseDateTime = getBaseDateTime()
        val baseDate = baseDateTime.first
        val baseTime = baseDateTime.second

        skiResortRepository.findAll().forEach { resort ->
            val nx = resort.xCoordinate
            val ny = resort.yCoordinate

            val url = buildVilageFcstUrl(baseDate, baseTime, nx, ny)
            val response = restTemplate.getForObject(url, String::class.java)
            val forecastData = parseVilageFcstResponse(response)

            // 시간대별 날씨 업데이트
            val hourlyWeathers = createHourlyWeather(resort, forecastData)
            hourlyWeatherRepository.deleteBySkiResort(resort)
            hourlyWeatherRepository.saveAll(hourlyWeathers)

            // 주간 날씨 업데이트
            updateShortTermDailyWeather(resort, forecastData)
        }
    }

    private fun getBaseDateTime(): Pair<String, String> {
        // 전날 23시 return(ex: 20241109 2300)
        val yesterday = LocalDateTime.now().minusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val baseTime = "2300"
        return Pair(yesterday, baseTime)
    }

    private fun buildVilageFcstUrl(baseDate: String, baseTime: String, nx: String, ny: String): String {
        return "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
                "?serviceKey=$apiKey" +
                "&pageNo=1" +
                "&numOfRows=1000" +
                "&dataType=JSON" +
                "&base_date=$baseDate" +
                "&base_time=$baseTime" +
                "&nx=$nx" +
                "&ny=$ny"
    }

    private fun parseVilageFcstResponse(response: String?): List<ForecastItem> {
        response ?: return emptyList()
        val rootNode = objectMapper.readTree(response)
        val itemsNode = rootNode["response"]["body"]["items"]["item"]
        val items = mutableListOf<ForecastItem>()

        itemsNode?.forEach { itemNode ->
            val category = itemNode["category"].asText()
            val fcstDate = itemNode["fcstDate"].asText()
            val fcstTime = itemNode["fcstTime"].asText()
            val fcstValue = itemNode["fcstValue"].asText()
            items.add(ForecastItem(category, fcstDate, fcstTime, fcstValue))
        }

        return items
    }

    data class ForecastItem(
        val category: String,
        val fcstDate: String,
        val fcstTime: String,
        val fcstValue: String
    )

    private fun createHourlyWeather(
        resort: SkiResort,
        forecastData: List<ForecastItem>
    ): List<HourlyWeather> {
        val hourlyWeathers = mutableListOf<HourlyWeather>()
        val timeSlots = generateTimeSlots()

        var priority = 1
        for (timeSlot in timeSlots) {
            val dataForTime = forecastData.filter { it.fcstDate == timeSlot.first && it.fcstTime == timeSlot.second }
            if (dataForTime.isEmpty()) continue

            val dataMap = dataForTime.groupBy { it.category }.mapValues { it.value.first().fcstValue }
            val temperature = dataMap["TMP"]?.toIntOrNull() ?: continue
            val precipitationChance = dataMap["POP"]?.toIntOrNull() ?: continue
            val sky = dataMap["SKY"]?.toIntOrNull() ?: 1
            val pty = dataMap["PTY"]?.toIntOrNull() ?: 0
            val condition = determineCondition(sky, pty)

            val forecastTime = formatForecastTime(timeSlot.second)
            val hourlyWeather = HourlyWeather(
                skiResort = resort,
                forecastTime = forecastTime,
                priority = priority,
                temperature = temperature,
                precipitationChance = precipitationChance,
                condition = condition
            )
            hourlyWeathers.add(hourlyWeather)
            priority++
        }
        return hourlyWeathers
    }

    private fun generateTimeSlots(): List<Pair<String, String>> {
        val timeSlots = mutableListOf<Pair<String, String>>()
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val format = DateTimeFormatter.ofPattern("yyyyMMdd")

        val times = listOf("0800", "1000", "1200", "1400", "1600", "1800", "2000", "2200", "0000", "0200")
        for (time in times) {
            val date = if (time == "0000" || time == "0200") tomorrow.format(format) else today.format(format)
            timeSlots.add(Pair(date, time))
        }
        return timeSlots
    }

    private fun formatForecastTime(fcstTime: String): String {
        val hour = fcstTime.substring(0, 2).toInt()
        val period = if (hour < 12) "오전" else "오후"
        val hourIn12 = if (hour == 0 || hour == 12) 12 else hour % 12
        return "$period ${hourIn12}시"
    }

    private fun determineCondition(sky: Int, pty: Int): String {
        return when (pty) {
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            4 -> "소나기"
            else -> when (sky) {
                1 -> "맑음"
                3 -> "구름많음"
                4 -> "흐림"
                else -> "맑음"
            }
        }
    }

    private fun updateShortTermDailyWeather(
        resort: SkiResort,
        forecastData: List<ForecastItem>
    ) {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val format = DateTimeFormatter.ofPattern("yyyyMMdd")

        val days = listOf(Pair(today, 0), Pair(tomorrow, 1))
        days.forEach { (date, dDay) ->
            val dateStr = date.format(format)
            val dataForDay = forecastData.filter { it.fcstDate == dateStr }
            if (dataForDay.isEmpty()) return@forEach

            // 최고 강수확률
            val popValues = dataForDay.filter { it.category == "POP" }.mapNotNull { it.fcstValue.toIntOrNull() }
            val precipitationChance = popValues.maxOrNull() ?: 0

            // 가장 나쁜 상태
            val conditions = dataForDay.filter { it.category == "SKY" || it.category == "PTY" }
                .groupBy { Pair(it.fcstDate, it.fcstTime) }
                .map { (_, items) ->
                    val sky = items.find { it.category == "SKY" }?.fcstValue?.toIntOrNull() ?: 1
                    val pty = items.find { it.category == "PTY" }?.fcstValue?.toIntOrNull() ?: 0
                    determineConditionPriority(sky, pty)
                }

            val worstCondition = conditions.maxByOrNull { it.priority }?.condition ?: "맑음"

            // 최저기온과 최고기온 계산
            val tmnValues =
                dataForDay.filter { it.category == "TMN" }.mapNotNull { it.fcstValue.toDoubleOrNull()?.toInt() }
            val tmxValues =
                dataForDay.filter { it.category == "TMX" }.mapNotNull { it.fcstValue.toDoubleOrNull()?.toInt() }

            val tmpValues =
                dataForDay.filter { it.category == "TMP" }.mapNotNull { it.fcstValue.toDoubleOrNull()?.toInt() }

            val minTemp = if (tmnValues.isNotEmpty()) tmnValues.minOrNull() ?: 0 else tmpValues.minOrNull() ?: 0
            val maxTemp = if (tmxValues.isNotEmpty()) tmxValues.maxOrNull() ?: 0 else tmpValues.maxOrNull() ?: 0

            // 주간 날씨 업데이트
            val existingWeather = dailyWeatherRepository.findBySkiResortAndDDay(resort, dDay)
            if (existingWeather != null) {
                existingWeather.forecastDate = date
                existingWeather.dayOfWeek = convertDayOfWeek(date.dayOfWeek.name)
                existingWeather.precipitationChance = precipitationChance
                existingWeather.condition = worstCondition
                existingWeather.minTemp = minTemp
                existingWeather.maxTemp = maxTemp
                dailyWeatherRepository.save(existingWeather)
            } else {
                val dailyWeather = DailyWeather(
                    skiResort = resort,
                    forecastDate = date,
                    dayOfWeek = convertDayOfWeek(date.dayOfWeek.name),
                    dDay = dDay,
                    precipitationChance = precipitationChance,
                    maxTemp = maxTemp,
                    minTemp = minTemp,
                    condition = worstCondition
                )
                dailyWeatherRepository.save(dailyWeather)
            }
        }
    }

    data class ConditionPriority(val condition: String, val priority: Int)

    private fun determineConditionPriority(sky: Int, pty: Int): ConditionPriority {
        return when (pty) {
            3 -> ConditionPriority("눈", 7)
            2 -> ConditionPriority("비/눈", 6)
            1 -> ConditionPriority("비", 5)
            4 -> ConditionPriority("소나기", 4)
            0 -> when (sky) {
                4 -> ConditionPriority("흐림", 3)
                3 -> ConditionPriority("구름많음", 2)
                1 -> ConditionPriority("맑음", 1)
                else -> ConditionPriority("맑음", 1)
            }

            else -> ConditionPriority("맑음", 1)
        }
    }
}
