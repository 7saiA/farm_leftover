package ashteam.farm_leftover.security.jwt;

import ashteam.farm_leftover.security.userSecurity.CustomUserDetails;
import ashteam.farm_leftover.security.userSecurity.CustomUserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserServiceImpl customUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtService.validateJwtToken(token)) {
            setCustomUserDetailsToSecurityContextHolder(token);
        }
        //passes the request further down the Spring Security filter chain
        //without a token, the request is considered anonymous (for .permitAll())
        filterChain.doFilter(request, response);
        //security context is cleared automatically after request processing
    }

    //this method authenticates the user in Spring Security and stores their data in the security context
    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        String login = jwtService.getLoginFromToken(token);
        //search for a user in the database, wrapping it in CustomUserDetails: login, hashPass, roles
        CustomUserDetails customUserDetails = customUserService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null, //we don't pass the password, as I understand it, because it is in CustomUserDetails
                customUserDetails.getAuthorities() //role
        );
        //writes authentication to global Spring security data store
        //after user is considered authorized
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(@NonNull HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            //7 = "Bearer "
            return bearerToken.substring(7);
        }
        return null;
    }
}
