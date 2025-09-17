package com.fqlopes.demonstration.repositories;




import com.fqlopes.demonstration.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//Implementação para banco de dados (agnóstico, SQL)

@Repository //Configura a interface como repositório de dados. Cuida das consultas, inserção, CRUD para o banco de dados
public interface RoleRepository extends JpaRepository<Role, Long> {
}
