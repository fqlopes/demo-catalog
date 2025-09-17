package com.fqlopes.demonstration.dto;

import com.fqlopes.demonstration.entities.Category;
import com.fqlopes.demonstration.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size (min = 5, max = 32, message = "Deve ter entre 5 à 32 caracteres")
    @NotBlank (message = "Campo Requirido")
    private String name;

    @NotBlank (message = "Campo Requirido")
    private String description;

    @Positive (message = "Preço precisa ser positivo")
    private Double price;
    private String imgUrl;

    @PastOrPresent (message = "Data não pode ser no futuro")
    private Instant date;

    //Configuração para relação many-to-many
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(){
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    public ProductDTO(Product entity, Set<Category> categories){
        this(entity);
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }
}
