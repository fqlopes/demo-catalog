package com.fqlopes.demonstration.services;


import com.fqlopes.demonstration.dto.ProductDTO;
import com.fqlopes.demonstration.entities.Category;
import com.fqlopes.demonstration.entities.Product;
import com.fqlopes.demonstration.repositories.CategoryRepository;
import com.fqlopes.demonstration.repositories.ProductRepository;

import com.fqlopes.demonstration.services.exceptions.DatabaseException;
import com.fqlopes.demonstration.services.exceptions.ResourceNotFoundException;
import com.fqlopes.demonstration.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    //Campos para teste
    @InjectMocks
    private ProductService service; //Esta classe é dependente de ProductRepository e CategoryRepository;

    @Mock //Com a anotação @Mock, será criado um objeto de "mentirinha", para ser utilizado
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    //Campos
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    // tipos concretos para simbolizar uma página de dados web, e um produto qualquer
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;




    @BeforeEach
    void setUp() throws Exception {
        //Configuração de comportamento desejado dos mocks

        //Campos a serem utilizados que simulam IDs podem possuir qualquer valor, desde que não sejam repetidos
        existingId = 1L;
        dependentId = 2L;
        nonExistingId = 3L;
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();

        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        // Configura via repository getReferenceById: retorna a entidade ou lança Exceptions se não existir
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //Configura getReferenceById do categoryRepository: retorna uma categoria existente ou lança Exception
        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //Configura o método findAll para retornar uma página contendo um produto qualquer simulado
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Configura o método save para retornar um produto qualquer e simulando persistência
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        //Configura o método findById para retornar um Optional contendo um produto quando seu ID existe
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        //Configura o método findById para retornar um Optional vazio quando o ID não existir
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Quando um ID existir e for chamado via existsById, retorna positivo para sua existência
        Mockito.when(repository.existsById(existingId)).thenReturn(true);

        //Configura o método existsById retorn falso quando chamado com id inexistente
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        //Configura o método para retornar como positivo quando chamado com um ID que possua dependências
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        //Configura o comportamento do erro para um produto com varias dependências e categorias
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result);

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {

            ProductDTO result = service.update(nonExistingId, productDTO);

        });

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenWhenIdDoesNotExist(){


        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = service.findById(nonExistingId);
        });
    }


    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        //Possibilidade de prever quantas vezes um método é chamado, comparado à quantas vezes deve ser chamado
        Mockito.verify(repository ,Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}
