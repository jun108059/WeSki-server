package nexters.weski.app.version

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "App 최소 버전, 강제업데이트 flag 데이터")
data class AppVersionResponseDto(
    // 예: "android", "ios" 등
    val platform: Platform,
    // 클라이언트에서 동작 가능한 최소 버전
    val minVersion: String,
    // true = App 강제 업데이트 대상임을 의미
    val isForceUpdate: Boolean = false,
)
