package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDB,Long> {

    Optional<UserDB> findByLogin(String login);


}
