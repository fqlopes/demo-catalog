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
@Entity
@Table (name = "tb_product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;


    //campos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT") //Habilitando descrições com muitas palavras
    private String description;
    private Double price;
    private String imgUrl;

    //campo para auditoria do banco de dados
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant date;

    //Um produto pode haver varias categorias, ao mesmo tempo que não pode haver repetições
    @ManyToMany
    @JoinTable (
            name = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Setter(AccessLevel.NONE)
    Set<Category> categories = new HashSet<>();

    public Product (){

    }

    public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }
}
