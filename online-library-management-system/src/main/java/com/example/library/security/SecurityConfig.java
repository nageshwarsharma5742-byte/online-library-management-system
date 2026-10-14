package com.example.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("librarian").password(passwordEncoder().encode("librarian123")).roles("LIBRARIAN")
            .and()
            .withUser("member").password(passwordEncoder().encode("member123")).roles("MEMBER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/books/**").permitAll()
                .antMatchers("/api/members/**").hasAnyRole("LIBRARIAN", "MEMBER")
                .antMatchers("/api/transactions/**").hasAnyRole("LIBRARIAN", "MEMBER")
                .antMatchers("/api/notifications/**").hasAnyRole("LIBRARIAN", "MEMBER")
                .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
            .headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

