package nexters.weski.snowmaker

data class SnowMakerDto(
    val resortId: Long,
    val totalVotes: Long,
    val positiveVotes: Long,
    val status: String,
)
