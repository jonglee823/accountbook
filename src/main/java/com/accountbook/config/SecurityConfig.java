package com.accountbook.config;

import com.accountbook.config.custom.SpringSessionRememberMeServices;
import com.accountbook.config.filter.EmailPasswordAuthFilter;
import com.accountbook.domain.User;
import com.accountbook.handler.Http401Handler;
import com.accountbook.handler.Http403Handler;
import com.accountbook.handler.LoginFailHandler;
import com.accountbook.handler.LoginSuccessHandler;
import com.accountbook.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.security.debug.mode:false}")
    private boolean isDebugMode;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers("/favicon.ico", "/error");
                web.debug(isDebugMode);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((requests) -> requests
//메서드 시큐리티 적용으로 인한 주석
//                                    .requestMatchers("/user").hasRole("USER")
//                                    .requestMatchers("/admin").access(
//                                    new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')")
//                                    )
                                    .anyRequest().permitAll()
                             )
                .csrf(AbstractHttpConfigurer::disable)
                //JSON 형태 로그인 구현을 위한 주석처리
//                .formLogin(form -> form
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/auth/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/")
//                        .failureHandler(new LoginFailHandler(objectMapper))
//                )
                .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( e->{
                                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                                    e.authenticationEntryPoint(new Http401Handler(objectMapper));
                                    })
                .userDetailsService(userDetailsService(userRepository))
                ;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username +"을 찾을 수 없습니다."));

                return new UserPrincipal(user);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new SCryptPasswordEncoder(65536, 8, 1, 32, 16);
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter(){
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        com.accountbook.config.custom.SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices(objectMapper);
        rememberMeServices.setRememberMeParameterName("remember");
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(7777);

        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}