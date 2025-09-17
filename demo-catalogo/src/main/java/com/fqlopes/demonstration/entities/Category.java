package com.fqlopes.demonstration.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity //Entidade: Cada linha da classe virará uma linha em uma tabela do banco de dados
@Table(name = "tb_category") // A entidade Category vai ser armazenada na tabela chamada tb_category
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id //Anotação para chave primária(PK). A chave primaria identifica cada linha da tabela, de forma unica
    @GeneratedValue (strategy = GenerationType.IDENTITY) //Auto-incrementando dentro do banco de dados para o campo "id"
    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    //Campos destinado para auditoria. Padronizado em UTC
    @Setter(AccessLevel.NONE) //Lombok: removendo setter deste campo
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //Registra o momento de uma transação com banco de dados
    private Instant createdAt;

    @Setter(AccessLevel.NONE)
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    //Equivale à lista de produtos dentro de uma categoria
    @ManyToMany(mappedBy = "categories") //Apontando para as relações feitas na classe Product
    @Setter(AccessLevel.NONE)
    private Set<Product> products = new HashSet<>();

    //Métodos para ao salvar uma categoria, gerar dados de auditoria (momento de criação/atualização)
    @PrePersist //É chamado sempre que cria-se uma entidade do tipo categoria
    public void prePersist () {
        this.createdAt = Instant.now();
    }

    @PreUpdate //É chamado sempre que houver alguma atualização de uma entidade de categoria
    public void preUpdate () {
        this.updatedAt = Instant.now();
    }

    //O JPA exige um construtor sem campos!
    public Category (){
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
