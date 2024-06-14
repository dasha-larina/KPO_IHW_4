package se.hse.auth_service.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import se.hse.auth_service.dto.LoginDTO
import se.hse.auth_service.dto.RegistrationDTO
import se.hse.auth_service.model.User
import se.hse.auth_service.service.UserService

@Tag(name = "Authentication", description = "Operations related to user authentication including registration, login, logout, and fetching user information.")
@RestController
@RequestMapping("/api/auth")
class AuthController(val userService: UserService) {

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Registers a new user with the provided information.")
    @ApiResponse(responseCode = "201", description = "User registered successfully", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "400", description = "Invalid data", content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = String::class)))])
    @ApiResponse(responseCode = "409", description = "The provided email is already in use", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = [Content(schema = Schema(implementation = String::class))])
    fun registerUser(@RequestBody @Valid body: RegistrationDTO) = userService.registerUser(body)

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates a user by email and password. On successful authentication, a JWT token is generated, returned, and saved in a cookie.")
    @ApiResponse(responseCode = "200", description = "User logged in successfully", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "403", description = "Incorrect password", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "404", description = "User with this email doesn't exist", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "500", description = "Internal server error", content = [Content(schema = Schema(implementation = String::class))])
    fun loginUser(@RequestBody @Valid body: LoginDTO, response: HttpServletResponse) = userService.loginUser(body, response)

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Logs out the currently logged-in user by clearing the JWT token cookie.")
    @ApiResponse(responseCode = "200", description = "User logged out successfully", content = [Content(schema = Schema(implementation = String::class))])
    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse) = userService.logoutUser(request, response)

    @GetMapping("/user")
    @Operation(summary = "Get User Information", description = "Retrieves information about the currently logged-in user based on the JWT token provided in the cookie.")
    @ApiResponse(responseCode = "200", description = "User found and information returned", content = [Content(schema = Schema(implementation = User::class))])
    @ApiResponse(responseCode = "401", description = "Invalid or expired JWT token", content = [Content(schema = Schema(implementation = String::class))])
    fun getUserInfo(request: HttpServletRequest) = userService.getUserInfo(request)
}
