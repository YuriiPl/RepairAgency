package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.ServiceName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceNameRepository extends JpaRepository<ServiceName,Long> {

}
