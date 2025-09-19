package com.fqlopes.demonstration.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqlopes.demonstration.dto.ProductDTO;
import com.fqlopes.demonstration.tests.Factory;
import com.fqlopes.demonstration.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc //Teste de integração na camada web. Carrega o contexto da aplicação, sem carregar o servidor
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc; //Permite que façamos as requisições sem os componentes do DB

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil; //Gerador de tokens dinamico para fins de teste
    private String username, password, bearerToken; //Campos utilizados pela classe que gera os tokens

    //campos
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;

        username = "maria@gmail.com";
        password = "123456";
        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

    }

    @Test
    public void findAllShouldReturnSortedPagedWhenSortByName() throws Exception{

        ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(25));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        ProductDTO productDTO = Factory.createProductDTO();

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ProductDTO productDTO = Factory.createProductDTO();

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }


}
