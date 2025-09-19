package com.fqlopes.demonstration.resources;

//Resources fará a implementação do controlador REST
//API disponibilizada pelo back-end

import com.fqlopes.demonstration.dto.CategoryDTO;
import com.fqlopes.demonstration.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    //@RequestParam -> Parametro não obrigatório
    @GetMapping //@GetMapping configura este metodo com endpoint na aplicação
    public ResponseEntity<Page<CategoryDTO>> findAll (Pageable pageable){

        log.info("LOG: FUI CHAMADO -> RETORNANDO TODA A LISTA");
        Page<CategoryDTO> list = service.findAllPaged(pageable);
        //ResponseEntity.ok() -> cria um ResponseEntity.BodyBuilder == HTTP com resposta 200 (OK)
        return ResponseEntity.ok().body(list);
    }

    //Adicionando rotas para cada ID criado
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById (@PathVariable Long id){
        log.info("LOG: FUI CHAMADO -> RETORNANDO POR ID");
        CategoryDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    //Inserção de Categorias é feito via objeto, que possui os dados pertinentes
    @PostMapping //Padrão REST: Ao inserir um novo recurso, usa-se o método POST
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<CategoryDTO> insert (@RequestBody CategoryDTO dto){
        dto = service.insert(dto);

        //Por padrdão, ao ser criado um novo recurso, retorna-se o código HTTP 201(Created),
        //juntamente com sua localização
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    //HTML PUT -> Atualizar um recurso dentro das categorias
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    //HTTP DELETE -> Remover categorias
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
