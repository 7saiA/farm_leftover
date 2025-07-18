package ashteam.farm_leftover.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/sing-in", "auth/register").permitAll()
                .requestMatchers("/users/farms/**").permitAll()
                .requestMatchers("/products/all-products").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/logout")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login"))
                .anyRequest().authenticated()
        );
        return http.build();
    }
}
