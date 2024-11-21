package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configure a relaxed HTTP firewall to allow specific encoded characters in URLs
    @Bean
    public HttpFirewall relaxedHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true); // Add any additional allowances if needed
        return firewall;
    }

    // Customizes the web security to apply the custom firewall
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall relaxedHttpFirewall) {
        return (web) -> web.httpFirewall(relaxedHttpFirewall);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (for testing purposes; enable in production)
            .csrf(csrf -> csrf.disable())
            
            // Allow all requests (in production, configure specific endpoints with .permitAll() and others with .authenticated())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            
            // Configure logout handling
            .logout(logout -> logout.logoutUrl("/perform_logout") // URL to trigger logout
                    .logoutSuccessUrl("/") // Redirect to homepage after logout
                    .deleteCookies("authToken") // Delete "authToken" cookie on logout
                    .invalidateHttpSession(true) // Invalidate the session on logout
                    .permitAll());

        return http.build();
    }

    // Bean to handle password encoding with BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
