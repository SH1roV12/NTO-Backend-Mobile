package ru.nto.team

import org.springframework.data.jpa.repository.JpaRepository


interface StaffRepository: JpaRepository<Staff,  Long> {
    fun findByCode(code: String): Staff?
}