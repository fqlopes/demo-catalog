package com.fqlopes.demonstration.services.validation;


import com.fqlopes.demonstration.dto.UserUpdateDTO;
import com.fqlopes.demonstration.entities.User;
import com.fqlopes.demonstration.repositories.UserRepository;
import com.fqlopes.demonstration.resources.exceptions.FieldMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//ConstraintValidator <Tipo da Anotação, Tipo de classe receberá anotação >
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request; //Guardando informações de uma requisição

    @Autowired
    private UserRepository repository; //Acessar dados requiridos para um repositório de usuários

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        var uriVars =(Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        //Lista com nossos erros customizados
        List<FieldMessage> list = new ArrayList<>();

        // Testes de validação, acrescentando objetos FieldMessage à lista

        //Validando usuário através de email
        User user = repository.findByEmail(dto.getEmail());
        if (user != null && userId != user.getId()){
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