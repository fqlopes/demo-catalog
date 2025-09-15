package com.fqlopes.demonstration.repositories;


import com.fqlopes.demonstration.entities.Product;
import com.fqlopes.demonstration.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest //Teste de uma classe de repositório, não é necessário carregar todo o contexto de aplicação
public class ProductRepositoryTests {

    //Variáveis a serem testadas
    @Autowired //Anotação spring pra injetar dependências
    private ProductRepository repository;
    private long existingId;
    private long nonExistentId;
    private long countTotalProducts;

    @BeforeEach //Método é executado antes de cada teste
    void setUp() throws Exception {
        existingId =1L;
        nonExistentId = 5000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalNonEmptyWhenIdExists(){


        Optional<Product>result  = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent(), "Deveria retornar um optional não vazio");

    }

    @Test
    public void findByIdShouldReturnEmptyProductWhenIdDoesNotExist(){

        Optional<Product> result = repository.findById(nonExistentId);
        Assertions.assertFalse(result.isPresent(), "Deveria retornar um optional vazio");

    }
}
