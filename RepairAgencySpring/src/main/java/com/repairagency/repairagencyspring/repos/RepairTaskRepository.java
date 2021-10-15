package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface RepairTaskRepository extends JpaRepository<RepairTask, Long> {
    Page<RepairTaskDTO> findByOwner(UserDB userDB, Pageable pageable);
    Optional<RepairTask> findByOwner_LoginAndIdAndWorkStatus(String name, Long id, WorkStatus done);
    Page<RepairTaskDTO> findAllByPayStatus(PayStatus payStatus, Pageable pageable);
    Optional<RepairTask> findByOwner_LoginAndIdAndPayStatus(String login, Long id, PayStatus status);
    Page<RepairTaskDTO> findAllByIdIsNotNull(Pageable pageable);
    Optional<RepairTask> findByIdAndWorkStatus(Long id,WorkStatus workStatus);
}