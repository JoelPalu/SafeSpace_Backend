package G2.SafeSpace.config;

import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import G2.SafeSpace.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userRepository;



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

        String username = jwtService.extractUsername(jwt);

        //4. set authenticate object inside security context
        User user = userRepository.findUserByUsername(username).get();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5. continue the filter chain
        filterChain.doFilter(request, response);

    }
}
