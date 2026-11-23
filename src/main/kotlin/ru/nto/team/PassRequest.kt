package ru.nto.team

import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


// TODO: JPA entity for pass_request, including a staff association and generated Long ID.
// No @Entity yet: the unfinished model must not prevent application startup.
@Entity
@Table(name = "pass_request")
class PassRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    var host: Staff,
    @Column(name = "full_name", length = 150, nullable = false)
    @field:Size(min = 5,max = 150)
    @field:NotBlank
    var fullName: String,

    @field:Pattern(regexp = "^\\+7[0-9]{10}$")
    @Column(name = "phone", length = 12, nullable = false)
    var phone: String,
    @Column(name = "organization", length = 150)
    var organization: String?,
    @Column(name = "purpose", length = 200, nullable = false)
    @field:Size(min = 5, max = 200)
    @field:NotBlank
    var purpose: String,
    @Column(name = "valid_from", nullable = false)
    var validFrom: LocalDateTime,
    @Column(name = "valid_to", nullable = false)
    var validTo: LocalDateTime,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    var status: RequestStatus,
    @Column(name = "comment", length = 500)
    var comment: String?,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
)


