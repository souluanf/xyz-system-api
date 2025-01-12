package dev.luanfernandes.config.security;

import static dev.luanfernandes.domain.constants.PathConstants.ANAGRAMS_V1;
import static dev.luanfernandes.domain.constants.PathConstants.AUTH_V1;
import static dev.luanfernandes.domain.constants.PathConstants.PLANTS_V1;
import static dev.luanfernandes.domain.constants.PathConstants.PLANTS_V1_ID;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_DELETE_BY_CITY;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_MORE_THAN_ONE_ORDER;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_TOTAL_SALES;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_UPDATE_NAMES;
import static dev.luanfernandes.domain.constants.PathConstants.SALES_PERSON_V1_WITHOUT_ORDERS;
import static dev.luanfernandes.domain.constants.PathConstants.USER_V1;
import static dev.luanfernandes.domain.constants.PathConstants.USER_V1_ID;
import static dev.luanfernandes.domain.enums.UserRole.ADMIN;
import static dev.luanfernandes.domain.enums.UserRole.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final RequestMatcher[] ALLOWED_PATHS = {
        antMatcher("/actuator/**"),
        antMatcher("/swagger-ui.html"),
        antMatcher("/swagger-ui/**"),
        antMatcher("/v3/api-docs/**"),
        antMatcher(AUTH_V1 + "/**"),
        antMatcher("/")
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.requestMatchers(ALLOWED_PATHS)
                        .permitAll()

                        // ANAGRAMS
                        .requestMatchers(POST, ANAGRAMS_V1)
                        .hasAnyRole(ADMIN.name(), USER.name())

                        // SALES PERSON
                        .requestMatchers(PUT, SALES_PERSON_V1_UPDATE_NAMES)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, SALES_PERSON_V1_WITHOUT_ORDERS)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, SALES_PERSON_V1_TOTAL_SALES)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, SALES_PERSON_V1_MORE_THAN_ONE_ORDER)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(DELETE, SALES_PERSON_V1_DELETE_BY_CITY)
                        .hasAnyRole(ADMIN.name(), USER.name())

                        // PLANTS
                        .requestMatchers(GET, PLANTS_V1)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(POST, PLANTS_V1)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, PLANTS_V1_ID)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(POST, PLANTS_V1_ID)
                        .hasRole(ADMIN.name())
                        .requestMatchers(PUT, PLANTS_V1_ID)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(DELETE, PLANTS_V1_ID)
                        .hasAnyRole(ADMIN.name(), USER.name())

                        // USERS
                        .requestMatchers(GET, USER_V1)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(POST, USER_V1)
                        .hasAnyRole(ADMIN.name())
                        .requestMatchers(GET, USER_V1_ID)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(POST, USER_V1_ID)
                        .hasRole(ADMIN.name())
                        .requestMatchers(PUT, USER_V1_ID)
                        .hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(DELETE, USER_V1_ID)
                        .hasAnyRole(ADMIN.name())
                        .anyRequest()
                        .permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtConverter())))
                .cors(withDefaults())
                .build();
    }
}
