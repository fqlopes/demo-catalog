package com.fqlopes.demonstration.dto;


import com.fqlopes.demonstration.services.validation.UserInsertValid;
import com.fqlopes.demonstration.services.validation.UserUpdateValid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UserUpdateValid
public class UserUpdateDTO extends UserDTO {
    private static final long serialVersionUID = 1L;

}
