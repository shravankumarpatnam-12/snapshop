package com.ecommerce.snapshop.security;


import com.ecommerce.snapshop.model.AppRole;
import com.ecommerce.snapshop.model.Role;
import com.ecommerce.snapshop.model.User;
import com.ecommerce.snapshop.repository.RoleRepository;
import com.ecommerce.snapshop.repository.UserRepository;
import com.ecommerce.snapshop.security.jwt.AuthEntryPoint;
import com.ecommerce.snapshop.security.jwt.AuthTokenFilter;
import com.ecommerce.snapshop.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.data.util.ClassUtils.ifPresent;
import java.util.Set;



@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

   @Autowired
    UserDetailsServiceImpl userDetailsService;

   @Autowired
    private AuthEntryPoint unauthorizedHandler;

   @Bean
    public AuthTokenFilter authenticationTokenFilterBean() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderBean() {
       DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
       provider.setUserDetailsService(userDetailsService);
       provider.setPasswordEncoder(passwordEncoder());
       return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfig) throws Exception {
       return authenticationConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/api/auth/**", "/V3/api-docs/**",
                                "/swagger-ui/**","/api/test/**",
                                "/images/**","/h2-console/**",
                                "/signin/**").permitAll().
                        anyRequest().authenticated());
        http.csrf(csrf -> csrf.disable())
       .exceptionHandling(expection ->
               expection.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));;
        http.headers(headers
                -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        http.authenticationProvider(authenticationProviderBean());
        http.addFilterBefore(authenticationTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
       return webSecurity -> webSecurity.ignoring().requestMatchers(
               "/configuration-ui/**",
               "/swagger-resources/**",
               "/configuration/security",
               "/swagger-ui.html",
               "/webjars/**",
               "/h2-console/**",
               "/api/auth/**,",
               "/signin/**"
       );
       }

//    @Bean
//    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            // Retrieve or create roles
//            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                    .orElseGet(() -> {
//                        Role newUserRole = new Role(AppRole.ROLE_USER);
//                        return roleRepository.save(newUserRole);
//                    });
//
//            Role sel lerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
//                    .orElseGet(() -> {
//                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
//                        return roleRepository.save(newSellerRole);
//                    });
//
//            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                    .orElseGet(() -> {
//                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
//                        return roleRepository.save(newAdminRole);
//                    });
//
//            Set<Role> userRoles = Set.of(userRole);
//            Set<Role> sellerRoles = Set.of(sellerRole);
//            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);
//
//
//            // Create users if not already present
//            if (!userRepository.existsByUsername("user1")) {
//                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
//                userRepository.save(user1);
//            }
//
//            if (!userRepository.existsByUsername("seller1")) {
//                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
//                userRepository.save(seller1);
//            }
//
//            if (!userRepository.existsByUsername("admin")) {
//                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
//                userRepository.save(admin);
//            }
//
//            if (userRepository.existsByUsername("user1")) {
//                User user = userRepository.findByUsername("user1").orElseThrow(
//                        ()->  new RuntimeException("user1 not found"));
//                user.setRoles(userRoles);
//                userRepository.save(user);
//            }
//
//            if (userRepository.existsByUsername("seller1")) {
//                User seller = userRepository.findByUsername("seller1").orElseThrow(
//                        ()->  new RuntimeException("seller not found"));
//                seller.setRoles(sellerRoles);
//                userRepository.save(seller);
//            }
//
//            if (userRepository.existsByUsername("admin")) {
//                User admin = userRepository.findByUsername("admin").orElseThrow(
//                        ()->  new RuntimeException("admin not found"));
//                admin.setRoles(adminRoles);
//                userRepository.save(admin);
//            }
//        };
//    }
}
