package se.hse.ticket_order_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import kotlin.io.encoding.ExperimentalEncodingApi

@Configuration
@ExperimentalEncodingApi
class SecurityConfiguration {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { c ->
                c
//                    .requestMatchers("/**", "/home", "/login", "/register", "/user").permitAll()
//                    .anyRequest().authenticated()
                    .anyRequest().permitAll()
            }
//            .userDetailsService(userProfileService)

        return http.build()
    }
}
