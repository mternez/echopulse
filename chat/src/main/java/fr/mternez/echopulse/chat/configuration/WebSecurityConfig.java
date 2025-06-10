package fr.mternez.echopulse.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain getSecurityFilterChain(final HttpSecurity http,
                                               final JwtAuthenticationConverter jwtAuthenticationConverter,
                                               final JwtDecoder jwtDecoder) throws Exception {

        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/ws").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) ->
                        oauth2.jwt(jwtConfigurer -> {
                            jwtConfigurer.decoder(jwtDecoder);
                            jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        })
                );
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtDecoder() {
        return new JwtAuthenticationConverter();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
