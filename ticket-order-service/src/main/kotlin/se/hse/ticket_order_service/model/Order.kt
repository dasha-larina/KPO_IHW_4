package se.hse.ticket_order_service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "\"order\"")
@JsonPropertyOrder(value = ["orderId", "userId", "fromStation", "toStation", "statusDescription", "created"])
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("orderId")
    var id: Int = 0,

    @Column(name = "user_id", nullable = false)
    private val userId: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_station_id", nullable = false)
    @JsonIgnoreProperties("hibernateLazyInitializer", "handler")
    private val fromStation: Station,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_station_id", nullable = false)
    @JsonIgnoreProperties("hibernateLazyInitializer", "handler")
    private val toStation: Station,

    @Column(name = "status", nullable = false)
    private var status: Int = 1,

    @Column(name = "created", nullable = false)
    private val created: LocalDateTime = LocalDateTime.now()
) {
    fun getUserId() = userId

    @JsonProperty("fromStation")
    fun getFromStation() = fromStation

    @JsonProperty("toStation")
    fun getToStation() = toStation

    @Transient
    @JsonProperty("statusDescription")
    fun getStatusDescription(): String = when (status) {
        1 -> "check"
        2 -> "success"
        3 -> "rejection"
        else -> "unknown"
    }

    fun getCreated() = created

    fun setStatus(newStatus: Int) {
        this.status = newStatus
    }
}
