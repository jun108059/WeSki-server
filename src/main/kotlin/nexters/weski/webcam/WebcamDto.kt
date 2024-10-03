package nexters.weski.webcam

data class WebcamDto(
    val name: String,
    val number: Int,
    val description: String?,
    val url: String?
) {
    companion object {
        fun fromEntity(entity: Webcam): WebcamDto {
            return WebcamDto(
                name = entity.name,
                number = entity.number,
                description = entity.description,
                url = entity.url
            )
        }
    }
}
