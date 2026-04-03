package github.arnavbansal2764.zorvyn_solution.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/dashboard/**").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers("/api/insights/**").hasAnyRole("ANALYST", "ADMIN")

                        // Records: read (GET) for Viewer+Analyst+Admin, write for Admin only
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/records/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers("/api/records/**").hasRole("ADMIN")

                        .requestMatchers("/api/manage/**").hasRole("ADMIN")

                        // Transactions: read (GET) for Viewer+Analyst+Admin, write for Admin only
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/transactions/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/transactions/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/transactions/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/transactions/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails viewer = User.builder()
                .username("viewer")
                .password(passwordEncoder().encode("password"))
                .roles("VIEWER")
                .build();
        UserDetails analyst = User.builder()
                .username("analyst")
                .password(passwordEncoder().encode("password"))
                .roles("ANALYST")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(viewer, analyst, admin);
    }
}