package com.fqlopes.demonstration.services.validation;

import com.fqlopes.demonstration.dto.UserInsertDTO;
import com.fqlopes.demonstration.entities.User;
import com.fqlopes.demonstration.repositories.UserRepository;
import com.fqlopes.demonstration.resources.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//ConstraintValidator <Tipo da Anotação, Tipo de classe receberá anotação >
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository; //Acessar dados requiridos para um repositório de usuários

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        //Lista com nossos erros customizados
        List<FieldMessage> list = new ArrayList<>();

        // Testes de validação, acrescentando objetos FieldMessage à lista

        //Validando usuário através de email
        User user = repository.findByEmail(dto.getEmail());
        if (user != null){
            list.add(new FieldMessage("email", "Email já existente"));
        }




        //Insere erros criados na lista para a validação de bean
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}