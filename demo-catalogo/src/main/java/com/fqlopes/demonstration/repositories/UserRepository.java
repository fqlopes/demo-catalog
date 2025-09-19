package com.fqlopes.demonstration.repositories;




import com.fqlopes.demonstration.entities.Product;
import com.fqlopes.demonstration.entities.User;
import com.fqlopes.demonstration.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


//Implementação para banco de dados (agnóstico, SQL)

@Repository //Configura a interface como repositório de dados. Cuida das consultas, inserção, CRUD para o banco de dados
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);


    @Query(nativeQuery = true, value = """
            SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleld, tb_role.authority
            FROM tb_user
            INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
            INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
            WHERE tb_user.email = :email
            """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
