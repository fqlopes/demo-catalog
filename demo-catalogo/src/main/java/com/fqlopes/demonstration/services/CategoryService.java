package com.fqlopes.demonstration.services;


import com.fqlopes.demonstration.dto.CategoryDTO;
import com.fqlopes.demonstration.entities.Category;
import com.fqlopes.demonstration.repositories.CategoryRepository;
import com.fqlopes.demonstration.services.exceptions.DatabaseException;
import com.fqlopes.demonstration.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service //Registra a classe como componente de injeção de dependências Spring -> gerenciamento de instâncias
public class CategoryService {

    //campos
    @Autowired //Aponta para o Spring forneça automaticamente a dependência necessaria, criando objetos deste tipo
    private CategoryRepository repository;

    //métodos
    //@Transactional -> configura a função para que faça uma transação com o banco de dados real
    @Transactional(readOnly = true) //readOnly: true -> não trava o banco de dados para entrada de novos dados
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> list = repository.findAll(pageRequest);

        //Transformando as entidades dentro da Category em CategoryDTO
        return list.map(CategoryDTO::new);
    }

    //Metodo para retornar categorias por ID
    @Transactional (readOnly = true)
    public CategoryDTO findById (Long id){
        Optional<Category> optionalCategory = repository.findById(id);
        Category entity = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Entidade não Encontrada"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        //Convertendo nosso DTO em uma entidade Category
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto){

        try {
            //Para atualizar um registro na JPA, é necessário instanciar um objeto com o registro em si
            Category entity = repository.getReferenceById(id); //instanciando sem atualizar o banco de dados
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);

        } catch (EntityNotFoundException e) {
            //Estaremos colhendo o erro antes que se propague, e então, lançaremos a nossa exceção declarada
            throw new ResourceNotFoundException("Id não foi encontrado! " + id);
        }
    }

    //Se já existir uma transação,o método faz parte do mesmo “pacote” de operações no banco de dados.
    //Se não existir transação nenhuma, ele roda sem transação e não cria uma nova.
    @Transactional( propagation = Propagation.SUPPORTS)
    public void delete (Long id){
        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }

    }
}
