package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface RepairTaskRepository extends PagingAndSortingRepository<RepairTask, Long> {
    Page<RepairTaskDTO> findByOwner(UserDB userDB, Pageable pageable);
}