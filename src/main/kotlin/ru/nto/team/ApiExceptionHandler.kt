package ru.nto.team

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nto.team.exceptions.ApiException
import ru.nto.team.exceptions.CannotFindStaffByCode
import ru.nto.team.exceptions.FieldValidationException
import ru.nto.team.exceptions.InvalidCode
import ru.nto.team.exceptions.InvalidStatus

// TODO: implement domain errors and validation errors. This only makes unfinished routes usable.

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(error: ApiException): ResponseEntity<Map<String, String>> {
        val message = error.message ?: "Error"
        return ResponseEntity
            .status(error.status)
            .body(mapOf("error" to message))
    }
    @ExceptionHandler(FieldValidationException::class)
    fun handleValidation(error: FieldValidationException): ResponseEntity<Map<String, Map<String, String>>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapOf("errors" to error.errors))
    }
}
