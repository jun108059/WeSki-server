package nexters.weski.webcam

data class WebcamDto(
    val name: String,
    val number: Int,
    val description: String?,
    val url: String?,
    val isExternal: Boolean?,
) {
    companion object {
        fun fromEntity(entity: Webcam): WebcamDto =
            WebcamDto(
                name = entity.name,
                number = entity.number,
                description = entity.description,
                url = entity.url,
                isExternal = isExternal(entity.url),
            )

        private fun isExternal(url: String?): Boolean =
            url?.let {
                !it.endsWith("m3u8")
            } ?: false
    }
}
