package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.PayStatus;
import com.repairagency.repairagencyspring.entity.RepairTask;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.entity.WorkStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairTaskRepository extends JpaRepository<RepairTask, Long> {
    Page<RepairTaskDTO> findByOwner(UserDB userDB, Pageable pageable);
    Optional<RepairTask> findByOwner_LoginAndIdAndWorkStatus(String name, Long id, WorkStatus done);
//    Page<RepairTaskDTO> findAllByPayStatus(PayStatus payStatus, Pageable pageable);
    Optional<RepairTask> findByOwner_LoginAndIdAndPayStatus(String login, Long id, PayStatus status);
    Page<RepairTaskDTO> findAllByIdIsNotNull(Pageable pageable);
    Optional<RepairTask> findByIdAndWorkStatus(Long id,WorkStatus workStatus);

    Page<RepairTaskDTO> findAllByRepairer_Login(String login, Pageable pageable);

    Optional<RepairTask> findByIdAndWorkStatusNot(Long id, WorkStatus workStatus);
}