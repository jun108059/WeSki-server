package nexters.weski.batch

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import nexters.weski.common.config.WeatherApiProperties
import nexters.weski.ski.resort.SkiResort
import nexters.weski.ski.resort.SkiResortRepository
import nexters.weski.weather.CurrentWeather
import nexters.weski.weather.CurrentWeatherRepository
import nexters.weski.weather.DailyForecast
import nexters.weski.weather.DailyWeather
import nexters.weski.weather.DailyWeatherRepository
import nexters.weski.weather.HourlyWeather
import nexters.weski.weather.HourlyWeatherRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.math.pow

@Service
class ExternalWeatherService(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val dailyWeatherRepository: DailyWeatherRepository,
    private val hourlyWeatherRepository: HourlyWeatherRepository,
    private val skiResortRepository: SkiResortRepository,
    private val weatherApiProperties: WeatherApiProperties,
) {
    val apiKey: String
        get() = weatherApiProperties.key

    val restTemplate = RestTemplate()
    val objectMapper = jacksonObjectMapper()

    @Transactional
    fun updateCurrentWeather() {
        val baseTime = getBaseTime()
        val baseLocalDateTime =
            if (baseTime == "2300") {
                LocalDateTime.now().minusDays(1)
            } else {
                LocalDateTime.now()
            }
        val baseDate = baseLocalDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        skiResortRepository.findAll().forEach { resort ->
            // 초단기 실황 API 호출
            val url =
                "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" +
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
        resort: SkiResort,
    ): CurrentWeather {
        val temperature = data["T1H"]?.toDoubleOrNull()?.toInt() ?: 0
        val windSpeed = data["WSD"]?.toDoubleOrNull() ?: 0.0
        val feelsLike = calculateFeelsLike(temperature, windSpeed)
        val condition = determineCondition(data)
        val description = generateDescription(condition, temperature)
        val dailyWeather =
            dailyWeatherRepository.findBySkiResortAndForecastDate(resort, LocalDate.now())
                ?: throw IllegalStateException("Daily weather not found for today")
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
            skiResort = resort,
        )
    }

    private fun calculateFeelsLike(
        temperature: Int,
        windSpeed: Double,
    ): Int =
        if (temperature <= 10 && windSpeed >= 4.8) {
            val feelsLike =
                13.12 + 0.6215 * temperature - 11.37 * windSpeed.pow(0.16) + 0.3965 * temperature *
                    windSpeed.pow(
                        0.16,
                    )
            feelsLike.toInt()
        } else {
            temperature
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

    private fun generateDescription(
        condition: String,
        temperature: Int,
    ): String {
        val prefix =
            when (condition) {
                "맑음" -> "화창하고"
                "구름많음" -> "구름이 많고"
                "흐림" -> "흐리고"
                "비" -> "비가 오고"
                "비/눈" -> "눈비가 내리고"
                "눈" -> "눈이 오고"
                else -> ""
            }

        val postfix =
            when {
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
        val baseDateTime23 = getYesterdayBaseDateTime23()
        val baseDate = baseDateTime23.first
        val baseTime = baseDateTime23.second

        skiResortRepository.findAll().forEach { resort ->
            val groupedMap =
                getShortTermDataGroupedByDate(
                    baseDate = baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    baseTime = baseTime,
                    nx = resort.xCoordinate,
                    ny = resort.yCoordinate,
                )
            // groupedMap: Map<LocalDate, List<JsonNode>>
            // key: 예보날짜, value: 해당 날짜의 시간별 항목들
            groupedMap.forEach { (date, items) ->
                // 3일 뒤(= 오늘 포함 최대 3~4일) 정도까지만 저장한다고 가정
                // 필요하면 조건문으로 필터링 가능
                if (date.isAfter(LocalDate.now().plusDays(3))) {
                    return@forEach // 3일 이후 데이터는 무시(예시)
                }

                val parsedDaily = parseDailyForecastByDay(date, items)

                // DB에서 (resort, date)로 기존 엔티티가 있는지 검색
                val existing = dailyWeatherRepository.findBySkiResortAndForecastDate(resort, date)
                if (existing != null) {
                    // update
                    existing.minTemp = parsedDaily.minTemp
                    existing.maxTemp = parsedDaily.maxTemp
                    existing.precipitationChance = parsedDaily.precipitationChance
                    existing.condition = parsedDaily.getCondition()

                    // dayOfWeek, dDay도 재계산
                    existing.dayOfWeek = convertDayOfWeek(date.dayOfWeek.name)
                    existing.dDay = calcDDay(date)

                    dailyWeatherRepository.save(existing)
                } else {
                    // insert
                    val newDaily =
                        DailyWeather(
                            forecastDate = date,
                            dayOfWeek = convertDayOfWeek(date.dayOfWeek.name),
                            dDay = calcDDay(date),
                            precipitationChance = parsedDaily.precipitationChance,
                            maxTemp = parsedDaily.maxTemp,
                            minTemp = parsedDaily.minTemp,
                            condition = parsedDaily.getCondition(),
                            skiResort = resort,
                        )
                    dailyWeatherRepository.save(newDaily)
                }
            }
            val detailedAreaCode = resort.detailedAreaCode
            val broadAreaCode = resort.broadAreaCode
            val baseDateTime18 = getYesterdayBaseDateTime18()
            val baseDate18 = baseDateTime18.first
            val baseTime18 = baseDateTime18.second
            val tmFc = baseDate18.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + baseTime18
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

    private fun getYesterdayBaseDateTime23(): Pair<LocalDate, String> {
        // 어제 날짜
        val yesterday = LocalDate.now().minusDays(1)
        val hour = 23 // 23시 기준
        return Pair(yesterday, String.format("%02d00", hour))
    }

    private fun getYesterdayBaseDateTime18(): Pair<LocalDate, String> {
        // 어제 날짜
        val yesterday = LocalDate.now().minusDays(1)
        val hour = 18 // 18시 기준
        return Pair(yesterday, String.format("%02d00", hour))
    }

    private fun buildMidTaUrl(
        areaCode: String,
        tmFc: String,
    ): String =
        "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" +
            "?serviceKey=$apiKey" +
            "&pageNo=1" +
            "&numOfRows=10" +
            "&dataType=JSON" +
            "&regId=$areaCode" +
            "&tmFc=$tmFc"

    private fun buildMidLandUrl(
        regId: String,
        tmFc: String,
    ): String =
        "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst" +
            "?serviceKey=$apiKey" +
            "&pageNo=1" +
            "&numOfRows=10" +
            "&dataType=JSON" +
            "&regId=$regId" +
            "&tmFc=$tmFc"

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
        midLandData: JsonNode?,
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

            // 1) 먼저 DB에서 (리조트, forecastDate)로 조회
            val existingWeather: DailyWeather? =
                dailyWeatherRepository.findBySkiResortAndForecastDate(resort, forecastDate)
            if (existingWeather != null) {
                existingWeather.dayOfWeek = convertDayOfWeek(dayOfWeek)
                existingWeather.precipitationChance = precipitationChance
                existingWeather.maxTemp = maxTemp
                existingWeather.minTemp = minTemp
                existingWeather.condition = condition
                existingWeather.dDay = i - 1
                dailyWeatherRepository.save(existingWeather)
                weatherList.add(existingWeather)
                continue
            } else {
                // 2) 없으면 새로 생성
                val dailyWeather =
                    DailyWeather(
                        skiResort = resort,
                        forecastDate = forecastDate,
                        dayOfWeek = convertDayOfWeek(dayOfWeek),
                        dDay = i - 1,
                        precipitationChance = precipitationChance,
                        maxTemp = maxTemp,
                        minTemp = minTemp,
                        condition = condition,
                    )
                weatherList.add(dailyWeather)
            }
        }

        return weatherList
    }

    private fun getPrecipitationChance(
        midLandData: JsonNode,
        day: Int,
    ): Int =
        when (day) {
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

    private fun getCondition(
        midLandData: JsonNode,
        day: Int,
    ): String =
        when (day) {
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

    /**
     * 오전/오후 예보 중 '우선순위가 더 나쁜' 쪽을 고르는 로직 (예: "비"가 "구름많음"보다 우선)
     * 상황에 맞게 우선순위를 조정할 수 있음
     */
    private fun selectWorseCondition(
        am: String,
        pm: String,
    ): String {
        val conditionPriority =
            listOf(
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
                "눈",
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

    private fun convertDayOfWeek(englishDay: String): String =
        when (englishDay) {
            "MONDAY" -> "월요일"
            "TUESDAY" -> "화요일"
            "WEDNESDAY" -> "수요일"
            "THURSDAY" -> "목요일"
            "FRIDAY" -> "금요일"
            "SATURDAY" -> "토요일"
            "SUNDAY" -> "일요일"
            else -> "ERROR"
        }

    @Transactional
    fun updateHourlyAndDailyWeather() {
        val baseDateTime = getBaseDateTime()
        val baseDate = baseDateTime.first
        val baseTime = baseDateTime.second

        skiResortRepository.findAll().forEach { resort ->
            val nx = resort.xCoordinate
            val ny = resort.yCoordinate

            // 단기예보조회
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
        val yesterday =
            LocalDateTime
                .now()
                .minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val baseTime = "2300"
        return Pair(yesterday, baseTime)
    }

    private fun buildVilageFcstUrl(
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String,
    ): String =
        "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
            "?serviceKey=$apiKey" +
            "&pageNo=1" +
            "&numOfRows=1000" +
            "&dataType=JSON" +
            "&base_date=$baseDate" +
            "&base_time=$baseTime" +
            "&nx=$nx" +
            "&ny=$ny"

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
        val fcstValue: String,
    )

    private fun createHourlyWeather(
        resort: SkiResort,
        forecastData: List<ForecastItem>,
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
            val hourlyWeather =
                HourlyWeather(
                    skiResort = resort,
                    forecastTime = forecastTime,
                    priority = priority,
                    temperature = temperature,
                    precipitationChance = precipitationChance,
                    condition = condition,
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

    private fun determineCondition(
        sky: Int,
        pty: Int,
    ): String =
        when (pty) {
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            4 -> "소나기"
            else ->
                when (sky) {
                    1 -> "맑음"
                    3 -> "구름많음"
                    4 -> "흐림"
                    else -> "맑음"
                }
        }

    private fun updateShortTermDailyWeather(
        resort: SkiResort,
        forecastData: List<ForecastItem>,
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
            val conditions =
                dataForDay
                    .filter { it.category == "SKY" || it.category == "PTY" }
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
            val existingWeather = dailyWeatherRepository.findBySkiResortAndForecastDate(resort, date)
            if (existingWeather != null) {
                existingWeather.forecastDate = date
                existingWeather.dayOfWeek = convertDayOfWeek(date.dayOfWeek.name)
                existingWeather.precipitationChance = precipitationChance
                existingWeather.condition = worstCondition
                existingWeather.minTemp = minTemp
                existingWeather.maxTemp = maxTemp
                dailyWeatherRepository.save(existingWeather)
            } else {
                val dailyWeather =
                    DailyWeather(
                        skiResort = resort,
                        forecastDate = date,
                        dayOfWeek = convertDayOfWeek(date.dayOfWeek.name),
                        dDay = dDay,
                        precipitationChance = precipitationChance,
                        maxTemp = maxTemp,
                        minTemp = minTemp,
                        condition = worstCondition,
                    )
                dailyWeatherRepository.save(dailyWeather)
            }
        }
    }

    data class ConditionPriority(
        val condition: String,
        val priority: Int,
    )

    private fun determineConditionPriority(
        sky: Int,
        pty: Int,
    ): ConditionPriority =
        when (pty) {
            3 -> ConditionPriority("눈", 7)
            2 -> ConditionPriority("비/눈", 6)
            1 -> ConditionPriority("비", 5)
            4 -> ConditionPriority("소나기", 4)
            0 ->
                when (sky) {
                    4 -> ConditionPriority("흐림", 3)
                    3 -> ConditionPriority("구름많음", 2)
                    1 -> ConditionPriority("맑음", 1)
                    else -> ConditionPriority("맑음", 1)
                }

            else -> ConditionPriority("맑음", 1)
        }

    /**
     * 단기 예보(최대 3일 후) 데이터를 가져오고, 날짜별로 묶어 반환
     * @param baseDate : 조회 기준 날짜(yyyyMMdd)
     * @param baseTime : 조회 기준 시간(HHmm)
     * @param nx, ny : 격자 좌표
     * @return Map<LocalDate, List<JsonNode>> 형태로, 날짜별로 아이템들을 묶어서 반환
     */
    fun getShortTermDataGroupedByDate(
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String,
    ): Map<LocalDate, List<JsonNode>> {
        // 단기예보조회 URL
        val url = buildShortTermUrl(baseDate, baseTime, nx, ny)
        val response = restTemplate.getForObject(url, String::class.java) ?: return emptyMap()

        val rootNode = objectMapper.readTree(response)
        val items = rootNode["response"]["body"]["items"]?.get("item") ?: return emptyMap()

        // 날짜별로 묶기 위한 맵
        val groupedMap = mutableMapOf<LocalDate, MutableList<JsonNode>>()

        for (item in items) {
            val fcstDateStr = item["fcstDate"].asText() // 예) "20250116"
            val localDate = LocalDate.parse(fcstDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"))

            // 아직 존재하지 않는 키면 새로운 리스트로 초기화
            val listForDate = groupedMap.getOrPut(localDate) { mutableListOf() }
            listForDate.add(item)
        }

        return groupedMap
    }

    private fun buildShortTermUrl(
        baseDate: String,
        baseTime: String,
        nx: String,
        ny: String,
    ): String =
        "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
            "?serviceKey=$apiKey" +
            "&pageNo=1" +
            "&numOfRows=1000" +
            "&dataType=JSON" +
            "&base_date=$baseDate" +
            "&base_time=$baseTime" +
            "&nx=$nx" +
            "&ny=$ny"

    /**
     * 날짜별 List<JsonNode> 에서 일최저/일최고 기온, POP, PTY, SKY 등을 추출해 DailyForecast 생성
     */
    fun parseDailyForecastByDay(
        date: LocalDate,
        items: List<JsonNode>,
    ): DailyForecast {
        val daily = DailyForecast(date = date)

        for (item in items) {
            val category = item["category"].asText() // 예: TMP, TMN, TMX, POP, PTY, SKY
            val fcstValue = item["fcstValue"].asText()

            when (category) {
                "TMP" -> {
                    val tmpVal = fcstValue.toIntOrNull() ?: continue
                    daily.minTemp = minOf(daily.minTemp, tmpVal)
                    daily.maxTemp = maxOf(daily.maxTemp, tmpVal)
                }

                "TMN" -> {
                    val tmnVal = fcstValue.toIntOrNull() ?: continue
                    daily.minTemp = tmnVal
                }

                "TMX" -> {
                    val tmxVal = fcstValue.toIntOrNull() ?: continue
                    daily.maxTemp = tmxVal
                }

                "POP" -> {
                    // 하루 중 가장 높은 강수확률을 그날 확률로 본다
                    val popVal = fcstValue.toIntOrNull() ?: 0
                    daily.precipitationChance = maxOf(daily.precipitationChance, popVal)
                }

                "PTY" -> {
                    // 강수형태 코드 중 '가장 안 좋은(큰) 값'을 우선
                    val ptyVal = fcstValue.toIntOrNull() ?: 0
                    daily.ptyCode = maxOf(daily.ptyCode, ptyVal)
                }

                "SKY" -> {
                    // 마찬가지로 SKY도 '가장 흐린(큰) 값'을 우선
                    // (1=맑음, 3=구름많음, 4=흐림)
                    val skyVal = fcstValue.toIntOrNull() ?: 1
                    daily.skyCode = maxOf(daily.skyCode, skyVal)
                }
            }
        }

        // 만약 TMN, TMX 둘 다 없었다면 TMP 기반의 min/maxTemp를 그대로 사용
        // 하나라도 있으면 해당 값을 우선으로(이미 위에서 대입)
        if (daily.minTemp == Int.MAX_VALUE) daily.minTemp = 0
        if (daily.maxTemp == Int.MIN_VALUE) daily.maxTemp = 0

        return daily
    }

    private fun calcDDay(forecastDate: LocalDate): Int {
        val today = LocalDate.now()
        return Period.between(today, forecastDate).days
    }
}
