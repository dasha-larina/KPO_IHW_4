package se.hse.auth_service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "\"user\"")
@JsonPropertyOrder(value = ["userId", "nickname", "email", "created"])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId")
    val id: Int = 0,

    @Column(name = "nickname", nullable = false)
    private val nickname: String,

    @Column(name = "email", nullable = false, unique = true)
    private val email: String,

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private val password: String,

    @Column(name = "created")
    private val created: LocalDateTime = LocalDateTime.now()
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword() = password

    @JsonProperty("nickname")
    override fun getUsername() = nickname

    fun getEmail() = email

    fun getCreated() = created
}
