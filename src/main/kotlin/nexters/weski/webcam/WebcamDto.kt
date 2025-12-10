package nexters.weski.webcam

data class WebcamDto(
    val id: Long,
    val name: String,
    val number: Int,
    val description: String?,
    val url: String?,
    val isExternal: Boolean?,
) {
    companion object {
        fun fromEntity(entity: Webcam): WebcamDto =
            WebcamDto(
                id = entity.id,
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
