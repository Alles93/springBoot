package com.igorpavlenkov.spring.pringboot.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final LoginSuccessHandler loginSuccessHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, LoginSuccessHandler loginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); // конфигурация для прохождения аутентификации
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.csrf().disable(); - попробуйте выяснить сами, что это даёт
/*        http.authorizeRequests()
                .antMatchers("/").permitAll() // доступность всем
                .antMatchers("/user").access("hasAnyRole('ROLE_USER')") // разрешаем входить на /user пользователям с ролью User
                .and().formLogin()  // Spring сам подставит свою логин форму
                .successHandler(loginSuccessHandler); // подключаем наш SuccessHandler для перенеправления по ролям
    }*/

            http.
                    csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/user").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                    .anyRequest().authenticated()
                    .and().formLogin()
                    .successHandler(loginSuccessHandler)
                    .permitAll()
                    .and()
                    .logout().permitAll();

        }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
