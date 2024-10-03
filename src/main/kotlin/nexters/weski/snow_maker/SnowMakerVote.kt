package nexters.weski.snow_maker


import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort
import java.time.LocalDateTime

@Entity
@Table(name = "snow_quality_votes")
data class SnowMakerVote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val isPositive: Boolean,
    val votedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
