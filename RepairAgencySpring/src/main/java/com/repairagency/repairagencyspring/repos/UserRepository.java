package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDto,Long> {

    Optional<UserDto> findByLogin( String login);


}
