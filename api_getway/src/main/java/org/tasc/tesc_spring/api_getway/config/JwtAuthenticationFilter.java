//package org.tasc.tesc_spring.api_getway.config;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.io.IOException;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final TokenValidator tokenValidator;
//
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("Authorization");
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7); // Loại bỏ phần "Bearer " trong token
//
//            // Xác thực token và lấy thông tin claims từ token
//            Claims claims = tokenValidator.isValidToken(token);
//            if (claims != null) {
//                // Tạo đối tượng Authentication từ thông tin trong token
//                String username = claims.getSubject();
//                List<SimpleGrantedAuthority> authorities = List.of(
//                        new SimpleGrantedAuthority("ROLE_" + claims.get("role"))
//                );
//
//                // Tạo đối tượng Authentication
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                        username, null, authorities
//                );
//
//                // Thiết lập chi tiết bổ sung (nếu cần)
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                // Đặt Authentication vào SecurityContextHolder
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }
//
//        // Tiến hành lọc tiếp theo trong chuỗi filter
//        filterChain.doFilter(request, response);
//    }
//}
