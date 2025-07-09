package ashteam.farm_leftover.security;

import ashteam.farm_leftover.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable) //use only jwt
                .csrf(AbstractHttpConfigurer::disable) //not needed for rest api with jwt
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/farms", "/users/farms/{login}",
                                "/auth/register", "/auth/sing-in").permitAll()
                        .requestMatchers("/products").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //turn off create sessions
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //put this filter before standard auth Spring Security
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        //min 10 for production (total: min 4 max 31)
        return new BCryptPasswordEncoder(12);
    }
}
