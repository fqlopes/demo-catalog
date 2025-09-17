package com.fqlopes.demonstration.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*
    Configuração de usuários

    O Usuário sempre saberá qual sua função (Role)
    Role por sua vez é mapeado
 */

@Getter
@Setter
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email; //login
    private String password;

    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(name = "tb_user_role",
               joinColumns = @JoinColumn(name = "user_id"), //Chave estrangeira (FK) referente à classe User
               inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
