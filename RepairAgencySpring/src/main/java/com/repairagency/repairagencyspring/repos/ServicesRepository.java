package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.Application;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends PagingAndSortingRepository<Application, Long> {

}