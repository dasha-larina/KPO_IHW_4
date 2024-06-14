package se.hse.auth_service.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<Any> {
        val errors = ex.bindingResult.fieldErrors.map { e -> "${e.field}: ${e.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

//    @ExceptionHandler(DataIntegrityViolationException::class)
//    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<String> {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body("The provided email is already in use.")
//    }
}
