/**
 * Filter to handle JWT authentication for incoming HTTP requests.
 * This filter extracts the JWT token from the `Authorization` header, validates it,
 * and sets the authenticated user in the Spring Security context.
 * Extends {@link OncePerRequestFilter} to ensure it is executed once per request.
 */
package G2.SafeSpace.config;

import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Processes each request to validate the JWT token and authenticate the user.
     *
     * @param request     the {@link HttpServletRequest} object that contains the client's request.
     * @param response    the {@link HttpServletResponse} object that contains the filter's response.
     * @param filterChain the {@link FilterChain} to pass the request/response to the next filter.
     * @throws ServletException if an error occurs during request processing.
     * @throws IOException      if an I/O error occurs.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1. obtain the token from the header

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. obtain the token
        String jwt = authHeader.split(" ")[1];

        //3. obtain subject in jwt
        String username = null;
        try{
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        //4. set authenticate object inside security context
        User user = userRepository.findByUsername(username).get();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5. continue the filter chain
        filterChain.doFilter(request, response);

    }
}
