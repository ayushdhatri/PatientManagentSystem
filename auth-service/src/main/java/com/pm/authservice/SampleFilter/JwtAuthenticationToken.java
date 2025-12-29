package com.pm.authservice.SampleFilter;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;

    public JwtAuthenticationToken(String token){
        super(Collections.emptyList());
        this.token = token;
        setAuthenticated(false);

    }

    public String getToken(){
        return token;
    }

    @Override
    public @Nullable Object getCredentials() {
        return token;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return null;
    }
}
