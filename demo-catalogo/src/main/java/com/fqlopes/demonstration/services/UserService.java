package com.fqlopes.demonstration.services;


import com.fqlopes.demonstration.dto.RoleDTO;
import com.fqlopes.demonstration.dto.UserDTO;
import com.fqlopes.demonstration.dto.UserInsertDTO;
import com.fqlopes.demonstration.dto.UserUpdateDTO;

import com.fqlopes.demonstration.entities.Role;
import com.fqlopes.demonstration.entities.User;

import com.fqlopes.demonstration.projections.UserDetailsProjection;
import com.fqlopes.demonstration.repositories.RoleRepository;
import com.fqlopes.demonstration.repositories.UserRepository;
import com.fqlopes.demonstration.services.exceptions.DatabaseException;
import com.fqlopes.demonstration.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    //campos
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> list = repository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional (readOnly = true)
    public UserDTO findById (Long id){
        Optional<User> optionalUser = repository.findById(id);
        User entity = optionalUser.orElseThrow(() -> new ResourceNotFoundException("Entidade não Encontrada"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDTOToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword())); //Senha será criptografada
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto){

        try {
            User entity = repository.getReferenceById(id);
            copyDTOToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id não foi encontrado! " + id);
        }
    }

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

    private void copyDTOToEntity(UserDTO dto,User entity){

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for(RoleDTO roleDTO : dto.getRoles()){
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        //A busca do usuário será feita diretamente no banco de dados

        if (result.isEmpty()){
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;


    }
}
