package se.hse.ticket_order_service.util

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import java.security.Key
import java.util.*

object JwtUtil {
    private const val KEY_STRING = "Yv7iffxV44WUUFVJG5EAzqKqRpZvg72hQgqiLbfXCLDLxLKFqi5muiy51RDTZzLg"
    private val SECRET_KEY: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY_STRING))

    fun extractToken(request: HttpServletRequest): String? {
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
}
