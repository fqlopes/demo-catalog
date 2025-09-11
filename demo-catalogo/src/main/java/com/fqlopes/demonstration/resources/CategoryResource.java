package com.fqlopes.demonstration.resources;

//Resources fará a implementação do controlador REST
//API disponibilizada pelo back-end

import com.fqlopes.demonstration.dto.CategoryDTO;
import com.fqlopes.demonstration.entities.Category;
import com.fqlopes.demonstration.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@SuppressWarnings("SpellCheckingInspection")
@RestController
@RequestMapping(value = "/categories") //rota REST -> website http://localhost:8080/categories
public class CategoryResource {

    //campos
    @Autowired
    private CategoryService service;

    //Criando endpoints
    //ResponseEntity => objeto spring. Encapsulamento de resposta HTTP (genérico)
    @GetMapping //@GetMapping configura este metodo com endpoint na aplicação
    public ResponseEntity<List<CategoryDTO>> findAll (){
        List<CategoryDTO> list = service.findAll();
        log.info("LOG: FUI CHAMADO -> RETORNANDO TODA A LISTA");
        //ResponseEntity.ok() -> cria um ResponseEntity.BodyBuilder == HTTP com resposta 200 (OK)
        return ResponseEntity.ok().body(list);
    }
}
