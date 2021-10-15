package com.repairagency.repairagencyspring.repos;

import com.repairagency.repairagencyspring.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
}
