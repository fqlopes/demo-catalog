package com.fqlopes.demonstration.resources;

//Resources fará a implementação do controlador REST
//API disponibilizada pelo back-end

import com.fqlopes.demonstration.dto.UserDTO;
import com.fqlopes.demonstration.dto.UserInsertDTO;
import com.fqlopes.demonstration.dto.UserUpdateDTO;
import com.fqlopes.demonstration.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/users") //http://localhost:8080/users
public class UserResource {

    //campos
    @Autowired
    private UserService service;

    //Criando endpoints
    //ResponseEntity → objeto spring. Encapsulamento de resposta HTTP (genérico)
    //@RequestParam → Parâmetro não obrigatório
    @GetMapping //@GetMapping configura este método com endpoint na aplicação
    public ResponseEntity<Page<UserDTO>> findAll (Pageable pageable){
        //O tipo Pageable(SPRING) faz a instanciação automática, sem necessitar a passada manual de todos os parâmetros
        //PARÂMETROS: page:page, size:linesPerPage, sort:direction, orderBy

        //Ao fazer uma busca paginada, declaramos parâmetros padrão como: numero da pagina, registros por pagina
        //ordenação, e ordem (ascendente ou descendente), sendo utilizados pelo PageRequest


        log.info("LOG: FUI CHAMADO -> RETORNANDO TODA A LISTA");
        Page<UserDTO> list = service.findAllPaged(pageable);
        //ResponseEntity.ok() -> cria um ResponseEntity.BodyBuilder == HTTP com resposta 200 (OK)
        return ResponseEntity.ok().body(list);
    }

    //Adicionando rotas para cada ID criado
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById (@PathVariable Long id){
        log.info("LOG: FUI CHAMADO -> RETORNANDO POR ID");

        UserDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    //Inserção de Categorias é feito via objeto, que possui os dados pertinentes
    @PostMapping //Padrão REST: Ao inserir um novo recurso, usa-se o método POST
    public ResponseEntity<UserDTO> insert (@Valid @RequestBody UserInsertDTO dto){
        UserDTO newDTO = service.insert(dto);
        //Por padrão, ao ser criado um novo recurso, retorna-se o código HTTP 201(Created),
        //juntamente com sua localização
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(newDTO);
    }

    //HTML PUT -> Atualizar um recurso dentro das categorias
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO dto){
        UserDTO newDTO = service.update(id, dto);
        return ResponseEntity.ok().body(newDTO);
    }

    //HTTP DELETE -> Remover categorias
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return  ResponseEntity.noContent().build();
    }


}
