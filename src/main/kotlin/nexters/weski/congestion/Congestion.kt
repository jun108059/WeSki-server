package nexters.weski.congestion

import jakarta.persistence.*
import nexters.weski.common.BaseEntity
import nexters.weski.ski_resort.SkiResort

@Entity
@Table(name = "congestion")
data class Congestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val time: String,
    val congestion: Int,
    val description: String, // 여유, 보통, 혼잡, 매우혼잡
    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort
) : BaseEntity()
