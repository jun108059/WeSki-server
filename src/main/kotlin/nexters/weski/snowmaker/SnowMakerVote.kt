package nexters.weski.snowmaker

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import nexters.weski.common.BaseEntity
import nexters.weski.ski.resort.SkiResort
import java.time.LocalDateTime

@Entity
@Table(name = "snow_quality_votes")
data class SnowMakerVote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val isPositive: Boolean,
    val votedAt: LocalDateTime = LocalDateTime.now(),
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort,
) : BaseEntity()
