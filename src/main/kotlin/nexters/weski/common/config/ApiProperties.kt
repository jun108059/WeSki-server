package nexters.weski.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 외부 API 속성을 관리하는 설정 클래스
 */
@Configuration
@ConfigurationProperties(prefix = "weather.api")
class WeatherApiProperties {
    /**
     * Weather API 키
     */
    lateinit var key: String
}

/**
 * TMap API 속성을 관리하는 설정 클래스
 */
@Configuration
@ConfigurationProperties(prefix = "tmap.api")
class TMapApiProperties {
    /**
     * TMap API 키
     */
    lateinit var key: String
}
