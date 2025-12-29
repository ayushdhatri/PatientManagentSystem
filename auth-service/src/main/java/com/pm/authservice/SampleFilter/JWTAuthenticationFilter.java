package com.pm.authservice.SampleFilter;//package com.pm.authservice.SampleFilter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pm.authservice.dto.LoginRequestDTO;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class JWTAuthenticationFilter extends OncePerRequestFilter {
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//    }
//
//   @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)  throws ServletException, IOException  {
//        // if request path does not contains /generate-token
//        if(request.getServletPath().equals("/generate-token")){
//            filterChain.doFilter(request, response);
//            return;
//        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        LoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
//
//       UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
//
//       Authentication authResult = authenticationManager.authenticate(authToken);
//
//       if(authResult.isAuthenticated()){
//           String token = jwtUtil.generateToken(authResult.getName(), 15);
//           response.setHeader("Authorization", "Bearer" + token);
//       }
//
//
//
//
//
//    }
//
//}

//import com.pm.authservice.dto.LoginRequestDTO;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.filter.OncePerRequestFilter;
//import tools.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//
//public class JWTAuthenticationFilter extends OncePerRequestFilter {
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // if path does not contains this then move on to next filter
//        if(!request.getServletPath().equals("/generate-token")){
//            filterChain.doFilter(request, response);
//            return;
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        LoginRequestDTO loginRequest = mapper.readValue(request.getInputStream(),LoginRequestDTO.class);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
//
//        Authentication authentication = authenticationManager.authenticate(authToken);
//
//        if(authentication.isAuthenticated()){
//            String token = jwtUtil.generateToken(authentication.getName(),15);
//            response.setHeader("Authorization", "Bearer" + token);
//        }
//
//
//
//    }
//}

import com.pm.authservice.dto.LoginRequestDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;



public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


   @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(!request.getServletPath().equals("/generate-token")){
            filterChain.doFilter(request, response);
            return;
        }

       ObjectMapper objMapper = new ObjectMapper();
       LoginRequestDTO loginRequest  = objMapper.readValue(request.getInputStream(), LoginRequestDTO.class);

       // now we need the authentication Provider
       UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

       Authentication authResult = authenticationManager.authenticate(authToken);

       if(authResult.isAuthenticated()){
           String token = jwtUtil.generateToken(authResult.getName(), 15);
           response.setHeader("Authorization", "Bearer" + token);
       }

   }

}