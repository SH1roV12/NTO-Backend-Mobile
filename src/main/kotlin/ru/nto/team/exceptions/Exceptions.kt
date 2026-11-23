package ru.nto.team.exceptions

open class ApiException(val status: Int, message: String) : RuntimeException(message)

class InvalidCode : ApiException(400, "Invalid code format")
class InvalidStatus : ApiException(400, "Invalid status")

class CannotFindStaffByCode : ApiException(401, "Unauthorized")

class CannotFindRequest : ApiException(404, "Request not found")

class DuplicateRequestException : ApiException(409, "Duplicate request")
class InvalidStatusTransitionException : ApiException(409, "Invalid status transition")

class FieldValidationException(val errors: Map<String, String>) : RuntimeException()
