package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.Service;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServiceRepository  extends PagingAndSortingRepository<Service,Long> {

}
