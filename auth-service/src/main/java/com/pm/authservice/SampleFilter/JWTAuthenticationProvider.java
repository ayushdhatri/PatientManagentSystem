package com.pm.authservice.SampleFilter;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthenticationProvider(JWTUtil jwtUtil, UserDetailsService userDetailsService){
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }



    @Override
    public @Nullable Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        // 1️⃣ Cast to JwtAuthenticationToken
        JwtAuthenticationToken jwtToken =
                (JwtAuthenticationToken) authentication;

        String token = jwtToken.getToken();

        // 2️⃣ Validate JWT & extract username
        String username = jwtUtil.validateAndExtractdUsername(token);
        if (username == null) {
            return null; // invalid or expired token
        }

        // 3️⃣ Load user from DB
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        // 4️⃣ Return authenticated Authentication
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}


