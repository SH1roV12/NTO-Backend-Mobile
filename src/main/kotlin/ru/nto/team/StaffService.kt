package ru.nto.team

import org.springframework.stereotype.Service
import ru.nto.team.exceptions.CannotFindStaffByCode
import ru.nto.team.exceptions.InvalidCode

@Service
class StaffService(private val repository: StaffRepository) {


    fun profile(code: String): ProfileDto {
        val staff: Staff = getStaffByCode(code)
        return ProfileDto(staff.fullName);
    }

    fun getStaffByCode(code: String): Staff {
        if (!RequestValidation.validateCode(code)) {
            throw InvalidCode()
        }
        return repository.findByCode(code)
            ?: throw CannotFindStaffByCode()
    }
}
