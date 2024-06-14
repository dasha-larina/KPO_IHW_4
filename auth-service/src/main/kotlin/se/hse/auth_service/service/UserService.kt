package se.hse.auth_service.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import se.hse.auth_service.dto.LoginDTO
import se.hse.auth_service.dto.RegistrationDTO
import se.hse.auth_service.model.Session
import se.hse.auth_service.model.User
import se.hse.auth_service.repository.SessionRepository
import se.hse.auth_service.repository.UserRepository
import se.hse.auth_service.util.JwtUtil

@Service
class UserService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val passwordEncoder: PasswordEncoder,
    ) {

    @Transactional
    fun registerUser(@RequestBody body: RegistrationDTO): ResponseEntity<Any> {
        if (userRepository.findByEmail(body.email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The provided email is already in use")
        }

        val encryptedPassword = passwordEncoder.encode(body.password)

        val newUser = User(
            nickname = body.nickname,
            email = body.email,
            password = encryptedPassword,
        )

        try {
            userRepository.save(newUser)
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully")
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
        }
    }

    @Transactional
    fun loginUser(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = userRepository.findByEmail(body.email) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email doesn't exist")

        if (!passwordEncoder.matches(body.password, user.password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect password")
        }

        val token = JwtUtil.generateToken(user.getEmail())

        val newSession = Session(
            user = user,
            token = token,
            expires = JwtUtil.getExpirationDateFromToken(token)
        )

        try {
            sessionRepository.save(newSession)
            JwtUtil.setJwtCookie(response, token)
            return ResponseEntity.status(HttpStatus.OK).body("You're logged in. Your session token has been securely stored in your browser cookies\nToken: $token")
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
        }
    }

    fun getUserInfo(request: HttpServletRequest): ResponseEntity<Any> {
        val jwtToken = JwtUtil.extractToken(request)

        if (jwtToken == null || !JwtUtil.validateToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired JWT token")
        }

        val email = JwtUtil.getEmailFromToken(jwtToken)
        val user = userRepository.findByEmail(email)

        return if (user != null) {
            ResponseEntity.status(HttpStatus.OK).body(user)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired JWT token")
        }
    }

    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Any> {
        val jwtToken = JwtUtil.extractToken(request)

        if (jwtToken == null || !JwtUtil.validateToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.OK).body("You're logged out")
        }

        JwtUtil.cleanJwtCookie(response)
        return ResponseEntity.status(HttpStatus.OK).body("You're logged out")
    }
}
