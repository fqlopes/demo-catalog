package com.fqlopes.demonstration.resources;

//Resources fará a implementação do controlador REST
//API disponibilizada pelo back-end

import com.fqlopes.demonstration.dto.ProductDTO;
import com.fqlopes.demonstration.services.ProductService;
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
@RequestMapping(value = "/products") //rota REST -> website http://localhost:8080/categories
public class ProductResource {

    //campos
    @Autowired
    private ProductService service;

    //Criando endpoints
    //ResponseEntity → objeto spring. Encapsulamento de resposta HTTP (genérico)
    //@RequestParam → Parâmetro não obrigatório
    @GetMapping //@GetMapping configura este método com endpoint na aplicação
    public ResponseEntity<Page<ProductDTO>> findAll (Pageable pageable){
        //O tipo Pageable(SPRING) faz a instanciação automática, sem necessitar a passada manual de todos os parâmetros
        //PARÂMETROS: page:page, size:linesPerPage, sort:direction, orderBy

        //Ao fazer uma busca paginada, declaramos parâmetros padrão como: numero da pagina, registros por pagina
        //ordenação, e ordem (ascendente ou descendente), sendo utilizados pelo PageRequest


        log.info("LOG: FUI CHAMADO -> RETORNANDO TODA A LISTA");
        Page<ProductDTO> list = service.findAllPaged(pageable);
        //ResponseEntity.ok() -> cria um ResponseEntity.BodyBuilder == HTTP com resposta 200 (OK)
        return ResponseEntity.ok().body(list);
    }

    //Adicionando rotas para cada ID criado
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById (@PathVariable Long id){
        log.info("LOG: FUI CHAMADO -> RETORNANDO POR ID");

        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    //Inserção de Categorias é feito via objeto, que possui os dados pertinentes
    @PostMapping //Padrão REST: Ao inserir um novo recurso, usa-se o método POST
    public ResponseEntity<ProductDTO> insert (@Valid @RequestBody ProductDTO dto){
        dto = service.insert(dto);

        //Por padrão, ao ser criado um novo recurso, retorna-se o código HTTP 201(Created),
        //juntamente com sua localização
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    //HTML PUT -> Atualizar um recurso dentro das categorias
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,@Valid @RequestBody ProductDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    //HTTP DELETE -> Remover categorias
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return  ResponseEntity.noContent().build();
    }


}
