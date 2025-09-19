package nexters.weski.ski.resort

import org.springframework.data.jpa.repository.JpaRepository

interface SkiResortRepository : JpaRepository<SkiResort, Long> {
    fun findAllByOrderByOpeningDateAsc(): List<SkiResort>

    fun findAllByOrderByResortIdAsc(): List<SkiResort>

    fun existsByName(name: String): Boolean
}
