package com.tui.proof.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Note CSRF is disabled here, this should be removed, but doing it
        // makes visual studio crash on testing PurchaseOrderController
        http.csrf().disable().authorizeRequests()
            .antMatchers("/orders/search").hasRole(ROLE_ADMIN)
            .and().httpBasic()
            .and().authorizeRequests()
            .antMatchers("/**").permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles(ROLE_USER).and()
                .withUser("admin").password("{noop}admin").roles(ROLE_USER, ROLE_ADMIN);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/h2-console/**");
    }

}
