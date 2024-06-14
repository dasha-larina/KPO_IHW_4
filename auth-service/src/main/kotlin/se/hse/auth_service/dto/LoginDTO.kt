package se.hse.auth_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class LoginDTO(
    @Schema(defaultValue = "default@default.ru")
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email cannot be empty")
    val email: String,

    @Schema(defaultValue = "ddDD77!!")
    @field:Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$", message = "Password must contain one digit, one lowercase, one uppercase, one special character, and must be at least 8 characters long")
    val password: String
)
