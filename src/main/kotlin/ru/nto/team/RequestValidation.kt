package ru.nto.team

import ru.nto.team.exceptions.FieldValidationException
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

data class ValidatedCreateData(
    val fullName: String,
    val phone: String,
    val organization: String?,
    val purpose: String,
    val validFrom: LocalDateTime,
    val validTo: LocalDateTime
)


// TODO: normalization, strict date parsing and collecting all available field errors.
object RequestValidation {
    fun validateCode(code: String): Boolean {
        return code.matches(Regex("^[A-Za-z0-9]{4}$"))
    }

    fun parseStatus(status: String): RequestStatus? {
        return RequestStatus.entries.find { it.name.equals(status, ignoreCase = false) }
    }



    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")
        .withResolverStyle(ResolverStyle.STRICT)
    private val PHONE_REGEX = Regex("^\\+7\\d{10}$")
    private val MOSCOW_ZONE = ZoneId.of("Europe/Moscow")
    fun validateCreate(body: CreateRequestBody, clock: Clock): ValidatedCreateData {
        val errors = mutableMapOf<String, String>()

        val fullName = body.fullName?.trim()
        if (fullName.isNullOrEmpty() || fullName.length !in 5..150) {
            errors["fullName"] = "ФИО должно содержать от 5 до 150 символов"
        }
        val phone = body.phone?.trim()

        if (phone == null || !phone.matches(PHONE_REGEX)) {
            errors["phone"] = "Телефон должен быть в формате +7XXXXXXXXXX"
        }
        val rawOrg = body.organization?.trim()
        val organization = if (rawOrg.isNullOrEmpty()) null else rawOrg
        if (organization != null && organization.length > 150) {
            errors["organization"] = "Организация не должна превышать 150 символов"
        }
        val purpose = body.purpose?.trim()
        if (purpose.isNullOrEmpty() || purpose.length !in 5..200) {
            errors["purpose"] = "Цель визита должна содержать от 5 до 200 символов"
        }
        val exactNow = LocalDateTime.now(clock.withZone(MOSCOW_ZONE))
        val validFrom = parseDate(body.validFrom?.trim())
        if (validFrom == null) {
            errors["validFrom"] = "Некорректная дата начала"
        } else if (validFrom.isBefore(exactNow)) {
            errors["validFrom"] = "Дата начала не может быть раньше текущего времени"
        }
        val validTo = parseDate(body.validTo?.trim())
        if (validTo == null) {
            errors["validTo"] = "Некорректная дата окончания"
        } else if (validFrom != null) {
            if (!validTo.isAfter(validFrom) || validFrom.toLocalDate() != validTo.toLocalDate()) {
                errors["validTo"] = "Дата окончания должна быть позже начала и в тот же день"
            }
        }
        if (errors.isNotEmpty()) {
            throw FieldValidationException(errors)
        }
        return ValidatedCreateData(
            fullName = fullName!!,
            phone = phone!!,
            organization = organization,
            purpose = purpose!!,
            validFrom = validFrom!!,
            validTo = validTo!!
        )
    }

    private fun parseDate(value: String?): LocalDateTime? {
        if (value.isNullOrEmpty()) return null
        return try {
            LocalDateTime.parse(value, DATE_FORMATTER)
        } catch (e: Exception) {
            null
        }
    }
}