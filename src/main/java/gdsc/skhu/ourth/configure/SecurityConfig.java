package gdsc.skhu.ourth.configure;

import gdsc.skhu.ourth.jwt.JwtFilter;
import gdsc.skhu.ourth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // rest api이므로 기본설정(비인증시 로그인폼 화면으로 리다이렉트) 사용 안함
                .httpBasic().disable()
                // rest api에서는 csrf 공격으로부터 안전하고 매 요청마다 csrf 토큰을 받지 않아도 되기에 disable()
                .csrf().disable()
                // ".STATELESS" 세션을 통한 인증 메커니즘을 사용하지 않겠다. (토큰 개념 사용하기 위함)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors().configurationSource(configurationSource())
                .and()

                // 시큐리티 처리에 HttpServletRequest를 이용함
                .authorizeHttpRequests()
                .requestMatchers("/main", "/login", "/join", "/").permitAll()
                // 인증 완료 후 [USER] 또는 [ADMIN] 권한을 가진 사용자만 접근 허용
                .requestMatchers("/user", "/usermission/add", "usermission/clear", "/rank/**", "/logout", "/badge/**").hasRole("USER")
                .requestMatchers("/mission", "/usermission", "/usermission/all").hasRole("ADMIN")
                // 이외에 모든 uri 요청은 인증을 완료해야 접근 허용
                .anyRequest().authenticated()

                .and()

                // JwtFilter를 UsernamePasswordAuthenticationFilter 이전에 등록하는 설정
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // PasswordEncoder: Spring Security에서 제공하는 인터페이스, 구현체를 빈으로 등록해야 사용 가능
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("https://ourth-frontend.vercel.app, https://ourth.duckdns.org", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Access-Control-Allow-Credentials", "Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}