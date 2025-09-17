package com.fqlopes.demonstration.config;

/*
    Classe de configuração de aplicação Spring

    Responsável por criar componentes específicos
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Bean //Demarca o método para ser gerenciado pelo SpringBoot, sendo injetado em outros componentes
    public BCryptPasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
}
