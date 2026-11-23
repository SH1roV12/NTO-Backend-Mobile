package ru.nto.team

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface PassRequestRepository: JpaRepository<PassRequest, Long> {
    fun findAllByHostIdOrderByValidFromAscIdAsc(hostId: Long): List<PassRequest>
    fun findAllByHostIdAndStatusOrderByValidFromAscIdAsc(hostId: Long,status: RequestStatus): List<PassRequest>
    @Query("""
        SELECT COUNT(r) > 0 
        FROM PassRequest r 
        WHERE r.host.id = :hostId 
          AND r.phone = :normalizedPhone 
          AND r.status IN :statuses 
          AND :newFrom < r.validTo 
          AND r.validFrom < :newTo
    """)
    fun existsDuplicate(hostId: Long, normalizedPhone: String, statuses: Collection<RequestStatus>, newFrom: LocalDateTime, newTo: LocalDateTime): Boolean
}