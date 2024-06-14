package se.hse.auth_service.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.security.Key
import java.util.*

object JwtUtil {
    private const val KEY_STRING = "Yv7iffxV44WUUFVJG5EAzqKqRpZvg72hQgqiLbfXCLDLxLKFqi5muiy51RDTZzLg"
    private val SECRET_KEY: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY_STRING))
    private const val EXPIRATION_TIME = 86400000 // 24 hours

    fun generateToken(email: String): String {
        val now = Date()
        val expiration = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .claim("sub", email)
            .claim("iat", now.time / 1000)
            .claim("exp", expiration.time / 1000)
            .signWith(SECRET_KEY)
            .compact()
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun setJwtCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie("JWT_TOKEN", token)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.maxAge = EXPIRATION_TIME
        response.addCookie(cookie)
    }

    fun cleanJwtCookie(response: HttpServletResponse) {
        val cookie = Cookie("JWT_TOKEN", null)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

    fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }

        val cookies = request.cookies ?: return null
        val jwtCookie = cookies.firstOrNull { it.name == "JWT_TOKEN" }
        return jwtCookie?.value
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)

            return !claims.body.expiration.before(Date())
        } catch (ex: JwtException) {
            return false
        }
    }

    fun getEmailFromToken(token: String): String {
        return getClaimsFromToken(token).subject
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimsFromToken(token).expiration
    }
}
