package com.example.product_listing_app.security;

import com.example.product_listing_app.service.UserService;
import com.example.product_listing_app.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter  {

    private final UserService userService;
    private final JWTUtil jwtUtil;

//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String token = getTokenFromHeader(request);
//        String username = JWTUtil.extractUsername(token);
//        // If token is not null and is valid, set the authentication in the context
//        if (token != null && JWTUtil.validateToken(token, username)) {
//
//            // Get the user details and set it in the context
//            UserDetails userDetails = userService.loadUserByUsername(username);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        filterChain.doFilter(request, response);
//
//    }

//    private String getTokenFromHeader(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

}

