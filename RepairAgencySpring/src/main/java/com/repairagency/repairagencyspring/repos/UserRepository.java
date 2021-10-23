package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDB,Long> {
    Optional<UserDB> findByLogin(String login);
    List<UserDB> findAllByUserRoleOrderByNameAsc(Role role);
    Optional<UserDB> findByIdAndUserRole(Long id, Role role);
}
