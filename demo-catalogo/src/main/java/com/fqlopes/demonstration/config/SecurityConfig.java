package com.fqlopes.demonstration.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/*
    Spring Boot 3.x + Spring Security 6.x
 */

//@Configuration
//@EnableWebSecurity //Ativando recursos
//public class SecurityConfig {
//
//    //Método para configurar como as requisições HTTP serão tratadas pelas segurança da aplicação
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//
//        //Qualquer requisição, de qualquer URL, será permitida sem exigir autorização
//        http.authorizeHttpRequests(autorize -> autorize.anyRequest().permitAll())
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }
//
//    //Método de configuração do Spring Security
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//
//        //Denomina quais formas de requisição serão ignoradas → todos os endpoints não passarão pela camada de segurança
//        return web -> web.ignoring().requestMatchers("/**");
//    }
//}