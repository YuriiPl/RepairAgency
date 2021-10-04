package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDto,Long> {

}
