package com.fqlopes.demonstration.entities;

/*
    Classe destinada à manutenção da autoridade usuários
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tb_role")
public class Role implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    public Role() {
    }

    public Role(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    //Override sobre o @Getter gerado pelo Lombok
    @Override
    public String getAuthority(){
        return authority;
    }


}
