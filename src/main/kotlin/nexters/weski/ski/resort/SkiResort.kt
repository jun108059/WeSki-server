package nexters.weski.ski.resort

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import nexters.weski.common.BaseEntity
import nexters.weski.slope.Slope
import nexters.weski.webcam.Webcam
import java.time.LocalDate

@Entity
@Table(name = "ski_resorts")
class SkiResort(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val resortId: Long = 0,
    var name: String,
    @Enumerated(EnumType.STRING)
    var status: ResortStatus,
    var openingDate: LocalDate? = null,
    var closingDate: LocalDate? = null,
    var openSlopes: Int = 0,
    var totalSlopes: Int = 0,
    var dayOperatingHours: String? = null,
    var nightOperatingHours: String? = null,
    var lateNightOperatingHours: String? = null,
    var dawnOperatingHours: String? = null,
    var midnightOperatingHours: String? = null,
    var snowfallTime: String? = null,
    var xCoordinate: String,
    var yCoordinate: String,
    var xRealCoordinate: Double? = null,
    var yRealCoordinate: Double? = null,
    var detailedAreaCode: String,
    var broadAreaCode: String,
    @OneToMany(mappedBy = "skiResort", fetch = jakarta.persistence.FetchType.LAZY)
    val slopes: List<Slope> = emptyList(),
    @OneToMany(mappedBy = "skiResort", fetch = jakarta.persistence.FetchType.LAZY)
    val webcams: List<Webcam> = emptyList(),
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SkiResort) return false
        return resortId != 0L && resortId == other.resortId
    }

    override fun hashCode(): Int = 31

    override fun toString(): String = "SkiResort(resortId=$resortId, name='$name')"
}

enum class ResortStatus {
    운영중,
    운영종료,
    예정,
}
