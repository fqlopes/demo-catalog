package com.fqlopes.demonstration.services;

import com.fqlopes.demonstration.dto.ProductDTO;
import com.fqlopes.demonstration.repositories.ProductRepository;
import com.fqlopes.demonstration.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
/*
  Teste de integração → IT = Integração/Integration Test

    @SpringBootTest = carrega o contexto da aplicação

    No teste de integração do ProductService, se comunicará com o ProductRepository, que deixará de ser
    simulado pelos Mocks

 */


@SpringBootTest
@Transactional //Testes passam a ser independentes entre si
public class ProductServiceIT {

    //Dependência real do ProductService não mais simulada por Mock
    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    //campos
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts; //Considerando que já saibamos a quantidade de entidades no banco

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L; //Por lidar com integração, devemos apontar um ID que realmente não exista
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {

        service.delete(existingId);

        //Asserção baseada numa situação real
        Assertions.assertEquals(countTotalProducts -1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
        {
           service.delete(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        //Teste para ver se pagina retorna
        Assertions.assertFalse(result.isEmpty());
        //Teste para saber se a pagina inicial é a mesma que declaramos
        Assertions.assertEquals(0, result.getNumber());
        //Teste para saber se o número de elementos dentro da página é igual à declaração
        Assertions.assertEquals(10, result.getSize());
        //Teste para saber se todos os elementos estão retornando pelo método
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {

        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        //Teste que simula uma busca com mais elementos que a pagina se propõe a mostrar
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedOrderedPageWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        //Teste que simula uma busca com mais elementos que a pagina se propõe a mostrar
        Assertions.assertFalse(result.isEmpty());
        //Manualmente testando a ordem que deve ser mostrada
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());

    }





}
