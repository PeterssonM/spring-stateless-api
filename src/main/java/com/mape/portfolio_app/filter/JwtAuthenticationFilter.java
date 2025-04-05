package com.mape.portfolio_app.filter;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mape.portfolio_app.exception.JwtAuthenticationException;
import com.mape.portfolio_app.util.JsonWebTokenUtil;
import com.mape.portfolio_app.util.JwtAuthenticationEntryPoint;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JsonWebTokenUtil jwtUtil;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/login") || path.startsWith("/register") || path.startsWith("/any")) {
            return true; 
        }
        return false;  
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {

        String jwt = getJWT(request.getCookies());

        if (jwt == null) {
            jwtAuthenticationEntryPoint.commence(request, response, new JwtAuthenticationException("No token found in cookies.", "cookie"));
            return;
        }
        
            /*
            * If the JWT is not null and the SecurityContextHolder does not already have an authentication object, it means that the request is not authenticated yet.
            * The Claims object throws JwtException if the token is invalid, expired, or malformed.
            */
        
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    Claims claims = Jwts.parser()
                        .verifyWith(jwtUtil.getSecretKey())
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
                
                    String personId = claims.getSubject();
                    GrantedAuthority authority = new SimpleGrantedAuthority(claims.get("role", String.class));
                            
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(personId, null, Collections.singletonList(authority));
                    
                    //future
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                
                } catch (Exception ex) {
                    jwtAuthenticationEntryPoint.commence(request, response, new JwtAuthenticationException("Invalid, expired, or malformed token.", "token", ex));
                    return;
                }
                
            }
        
        System.out.println("\nJWT filter passed.\n");
        chain.doFilter(request, response);
    }
    
    private String getJWT(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue(); 
                }
            }
        }
        return null;
    }
}