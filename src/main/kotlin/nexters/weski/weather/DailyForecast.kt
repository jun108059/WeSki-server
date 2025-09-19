package nexters.weski.weather

import java.time.LocalDate

data class DailyForecast(
    val date: LocalDate,
    var minTemp: Int = Int.MAX_VALUE,
    var maxTemp: Int = Int.MIN_VALUE,
    var precipitationChance: Int = 0,
    var ptyCode: Int = 0, // PTY: 강수형태 코드
    var skyCode: Int = 1, // SKY: 기본값을 맑음(1)으로
) {
    // 최종 condition 계산
    fun getCondition(): String {
        // PTY가 0이 아니면 비/눈/소나기 등
        // PTY 코드 매핑은 상황에 맞게 더 정교화 가능
        if (ptyCode != 0) {
            return when (ptyCode) {
                1 -> "비"
                2 -> "비/눈"
                3 -> "눈"
                4 -> "소나기" // 단기예보에서 4는 소나기
                else -> "기타강수"
            }
        } else {
            // PTY = 0인 경우 SKY 코드 확인
            return when (skyCode) {
                1 -> "맑음"
                3 -> "구름많음"
                4 -> "흐림"
                else -> "맑음"
            }
        }
    }
}
