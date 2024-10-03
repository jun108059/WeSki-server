package nexters.weski.webcam

import org.springframework.data.jpa.repository.JpaRepository

interface WebcamRepository : JpaRepository<Webcam, Long> {
    fun findAllBySkiResortResortId(resortId: Long): List<Webcam>
}