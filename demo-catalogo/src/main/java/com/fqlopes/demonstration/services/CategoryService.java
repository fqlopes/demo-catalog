package com.fqlopes.demonstration.services;


import com.fqlopes.demonstration.entities.Category;
import com.fqlopes.demonstration.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Registra a classe como componente de injeção de dependências Spring -> gerenciamento de instâncias
public class CategoryService {

    //campos

    @Autowired //Aponta para o Spring forneça automaticamente a dependência necessaria, criando objetos deste tipo
    private CategoryRepository repository;

    //métodos

    public List<Category> findAll(){
        return repository.findAll();
    }

}
