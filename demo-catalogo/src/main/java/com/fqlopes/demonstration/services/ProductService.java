package com.fqlopes.demonstration.services;


import com.fqlopes.demonstration.dto.ProductDTO;
import com.fqlopes.demonstration.entities.Category;
import com.fqlopes.demonstration.entities.Product;
import com.fqlopes.demonstration.repositories.CategoryRepository;
import com.fqlopes.demonstration.repositories.ProductRepository;
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

import java.util.Optional;

@Service
public class ProductService {

    //campos
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    //métodos
    //@Transactional -> configura a função para que faça uma transação com o banco de dados real
    @Transactional(readOnly = true) //readOnly: true -> não trava o banco de dados para entrada de novos dados
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = repository.findAll(pageRequest);

        //Transformando as entidades dentro da Product em ProductDTO
        return list.map(ProductDTO::new);
    }

    //Metodo para retornar categorias por ID
    @Transactional (readOnly = true)
    public ProductDTO findById (Long id){
        Optional<Product> optionalProduct = repository.findById(id);
        Product entity = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Entidade não Encontrada"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        //Convertendo nosso DTO em uma entidade Product
        Product entity = new Product();
        copyDTOToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){

        try {
            //Para atualizar um registro na JPA, é necessário instanciar um objeto com o registro em si
            Product entity = repository.getReferenceById(id); //instanciando sem atualizar o banco de dados
            copyDTOToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);

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

    private void copyDTOToEntity(ProductDTO dto,Product entity){

        //Dados presentes do produto. ID não é passado pois é gerenciado pelo banco de dados
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        //Garantindo que seja copiado apenas as categorias que acompanhem o objeto DTO
        entity.getCategories().clear();
        for(var catDTO : dto.getCategories()){
            Category category = categoryRepository.getReferenceById(catDTO.getId());
            entity.getCategories().add(category);
        }
    }
}
