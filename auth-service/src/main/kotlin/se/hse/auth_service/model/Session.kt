package se.hse.auth_service.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "session")
class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private var user: User,

    @Column(name = "token", nullable = false)
    private var token: String,

    @Column(name = "expires", nullable = false)
    private var expires: Date
)
