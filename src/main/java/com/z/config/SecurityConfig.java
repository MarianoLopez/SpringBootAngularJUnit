/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.config;

import com.z.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Mariano
 */

/*configuración del modulo spring security*/
@Configuration //avisa al spring container que debe examinar esta clase al momento de generar beans
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) //basically keeps all the defaults set by Spring Boot, only overriding them in this file.
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetails;//Servicio de autenticación de usuarios desde DB
  

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")/*indicar url del html login*/
                .failureUrl("/login?error=1")/*indicar url para errores*/
                .usernameParameter("username")/*nombre del parametro a recibir*/
                .passwordParameter("password")
                .and()
            .logout()
                .logoutUrl("/logout")/*url de logout*/
                .logoutSuccessUrl("/")/*al salir donde debe ir*/
                .and()
            .authorizeRequests()
                .antMatchers("/").hasAnyRole("USER","ADMIN")
                .and()
            .authorizeRequests()
                .antMatchers("/users/**").hasAnyRole("USER","ADMIN")/*cualquier request a /user/**.. debe tener ROL user o ADMIN*/
                .and();
            //.exceptionHandling().accessDeniedPage("/error/403");
    }
    
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails);//usuarios desde DB
        auth.inMemoryAuthentication().withUser("god").password("god").roles("ADMIN");//usuario estatico
        auth.authenticationProvider(authenticationProvider());//configuración de autenticación
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetails);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
   
   
    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
   
}
