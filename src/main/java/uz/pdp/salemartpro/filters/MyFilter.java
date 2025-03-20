package uz.pdp.salemartpro.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.salemartpro.entity.Role;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.repo.UserRepository;
import uz.pdp.salemartpro.security.JwtService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class MyFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public MyFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("token");
        if(token!=null){
            if (jwtService.isValid(token)) {
                String username = jwtService.getUsername(token);
                List<Role> roles = jwtService.getRoles(token);

                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(username)
                        .authorities(roles)
                        .password("") // No password needed for JWT-based auth
                        .build();

                var auth=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        roles
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Received Token: " + token);
                System.out.println("Token Validity: " + jwtService.isValid(token));
                System.out.println("Roles in Token: " + jwtService.getRoles(token));

            }

        }
        filterChain.doFilter(request, response);
    }
}
