package com.fqlopes.demonstration.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(exclude = "name")
@Entity //Entidade: Cada linha da classe virará uma linha em uma tabela do banco de dados
@Table(name = "tb_category") // A entidade Category vai ser armazenada na tabela chamada tb_category
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id //Anotação para chave primária(PK). A chave primaria identifica cada linha da tabela, de forma unica
    @GeneratedValue (strategy = GenerationType.IDENTITY) //Auto-incrementando dentro do banco de dados para o campo "id"
    private Long id;
    private String name;

    //O JPA exige um construtor sem campos!
    public Category (){
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
