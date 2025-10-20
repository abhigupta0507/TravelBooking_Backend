package com.example.demo.filter;

import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                email = jwtUtil.getEmailFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
                sendUnauthorized(response, "Unable to get JWT Token");
                return;
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
                sendUnauthorized(response, "JWT Token has expired");
                return;
            } catch (Exception e) {
                logger.error("Error parsing JWT Token", e);
                sendUnauthorized(response, "Invalid JWT Token");
                return;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtil.isValidToken(jwtToken)) {
                    UserDetails userDetails = User.builder()
                            .username(email)
                            .password("")
                            .authorities(new ArrayList<>())
                            .build();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    logger.error("JWT Token invalid");
                    sendUnauthorized(response, "JWT Token invalid");
                    return;
                }
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
                sendUnauthorized(response, "JWT Token has expired");
                return;
            } catch (Exception e) {
                logger.error("JWT Token Invalid or expired!", e);
                sendUnauthorized(response, "JWT Token Invalid or expired!");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String payload = String.format("{\"error\":\"%s\"}", message.replace("\"", "\\\""));
        response.getWriter().write(payload);
        response.getWriter().flush();
    }

}