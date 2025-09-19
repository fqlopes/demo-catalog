package com.fqlopes.demonstration.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email; //login
    private String password;

    @Setter(AccessLevel.NONE)
    @ManyToMany (fetch = FetchType.EAGER) //Força a comunicação entre USER x ROLE préviamente
    @JoinTable(name = "tb_user_role",
               joinColumns = @JoinColumn(name = "user_id"), //Chave estrangeira (FK) referente à classe User
               inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    //Cada <Role> representa um tipo de conta/autoridade para cada objeto User
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

    //Adicionando novos Roles
    public void addRole(Role role){
        roles.add(role);
    }

    //Verifica se dado usuário terá um role
    public boolean hasRole(String roleName){
        for(Role role : roles){
            if (role.getAuthority().equals(roleName)){
                return true;
            }
        }
        return false;

    }


    //Métodos interface UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
