package nexters.weski.webcam

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import nexters.weski.common.BaseEntity
import nexters.weski.ski.resort.SkiResort

@Entity
@Table(name = "webcams")
data class Webcam(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val number: Int,
    val description: String?,
    val url: String,
    @ManyToOne
    @JoinColumn(name = "resort_id")
    val skiResort: SkiResort,
) : BaseEntity()
