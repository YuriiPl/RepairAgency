package com.repairagency.repairagencyspring.model.DAO;

import com.repairagency.repairagencyspring.entity.UserAccount;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserAccountRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Slf4j
public class BalanceDAO {


    private final UserRepository userRepository;

    @Autowired
    public BalanceDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addAmount(String login, long amount) throws BalanceTransactionException {
        log.warn("TRANSACTION STARTS");
        UserDB user = userRepository.findByLogin(login).get();
        if (user == null) {
            throw new BalanceTransactionException("Account not found " + login);
        }
        long newBalance = user.getAccount().getAmount() + amount;
        user.getAccount().setAmount(newBalance);
        log.warn("TRANSACTION FINISHES");
    }


}