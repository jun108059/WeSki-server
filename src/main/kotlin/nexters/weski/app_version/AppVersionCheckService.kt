package nexters.weski.app_version

import org.springframework.stereotype.Service

@Service
class AppVersionCheckService {
    companion object {
        const val IOS_MIN_VERSION = "3.0.1"
        const val ANDROID_MIN_VERSION = "3.0.1"
    }
    fun getAppVersion(
        platform: Platform,
        version: String,
    ): AppVersionResponseDto {
        val minVersion = when (platform) {
            Platform.IOS -> IOS_MIN_VERSION
            Platform.ANDROID -> ANDROID_MIN_VERSION
            else -> throw IllegalArgumentException("Unsupported platform: $platform")
        }
        return AppVersionResponseDto(
            platform = platform,
            minVersion = minVersion,
            isForceUpdate = isForceUpdate(platform, version),
        )
    }

    private fun isForceUpdate(
        platform: Platform,
        version: String,
    ): Boolean {
        return when (platform) {
            Platform.IOS -> isForceUpdateForIOS(version)
            Platform.ANDROID -> isForceUpdateForAndroid(version)
            else -> throw IllegalArgumentException("Unsupported platform: $platform")
        }
    }

    private fun isForceUpdateForIOS(version: String): Boolean {
        return version < IOS_MIN_VERSION
    }

    private fun isForceUpdateForAndroid(version: String): Boolean {
        return version < ANDROID_MIN_VERSION
    }
}