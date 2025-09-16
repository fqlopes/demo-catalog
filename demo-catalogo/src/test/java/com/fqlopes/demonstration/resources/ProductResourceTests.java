package com.fqlopes.demonstration.resources;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqlopes.demonstration.dto.ProductDTO;
import com.fqlopes.demonstration.services.ProductService;
import com.fqlopes.demonstration.services.exceptions.DatabaseException;
import com.fqlopes.demonstration.services.exceptions.ResourceNotFoundException;
import com.fqlopes.demonstration.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @Autowired
    private ObjectMapper objectMapper;


    //Testes da camada web necessitam fazer requisições ao endpoint, utilizaremos MockMvc
    @Autowired
    private MockMvc mockMvc;

    //Dependência do ProductResource, mock para não acessar o banco de dados real
    @MockitoBean
    private ProductService service;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;


    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        //Simulando o comportamento do método findAllPaged de nosso Service
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Simulando o comportamento do método findById: se existe retorna productDTO, ou exceção caso não exista
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //Simulando o comportamento do service.update
        Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any()))
                .thenReturn(productDTO);
        Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any()))
                .thenThrow(ResourceNotFoundException.class);


        //Simulando o comportamento do ProductService.insert
        //Recebe um DTO, salva e retorna-o novamente
        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);

        //Simulando o comportamento de service.delete
        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);


    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception{
        //insert utiliza um service que recebe DTO, retorna código HTTP 201 se bem sucedido + productDTO
        //Se o ID não existir, joga um ResourceNotFoundException

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());

    }


    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
        //utiliza um service que recebe DTO e um ID e retorna um ResponseEntity do tipo DTO
        //Se o ID não existir, joga um ResourceNotFoundException

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenIdDoesNotExist() throws Exception{
        //utiliza um service que recebe DTO e um ID e retorna um ResponseEntity do tipo DTO
        //Se o ID não existir, joga um ResourceNotFoundException

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());


    }



    @Test
    public void findAllShouldReturnPage() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());

        //Campos de JSON presentes na pagina web
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));



    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
