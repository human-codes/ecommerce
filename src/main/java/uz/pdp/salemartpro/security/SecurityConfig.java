package uz.pdp.salemartpro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.salemartpro.filters.MyFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService, MyFilter myFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(cors -> cors.configurationSource());
        http.cors(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(requests ->
                requests
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                        .requestMatchers("/", "/store.html", "/static/**", "/js/**", "/css/**", "/images/**").permitAll()
                        .requestMatchers("/api/login","/api/file/**","/api/categories","/api/user/**","/api/product/**","/api/location/check").permitAll()
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/order/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/all", "/api/order/user/**", "/api/orderitems/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest()
                        .authenticated());

        http.userDetailsService(customUserDetailsService);
        http.addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean public AuthenticationProvider authprovider(CustomUserDetailsService customUserDetailsService){
        var providerManager = new DaoAuthenticationProvider(
                passwordEncoder()
        );
        providerManager.setUserDetailsService(customUserDetailsService);
        return providerManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider){
        return new ProviderManager(authenticationProvider);
    }
}
