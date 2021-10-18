package com.repairagency.repairagencyspring.DAO;

import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BalanceDAO {

    private final UserRepository userRepository;

    @Autowired
    public BalanceDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public long addAmount(String login, long amount) throws BalanceTransactionException {
        log.warn("TRANSACTION STARTS");
        UserDB user = userRepository.findByLogin(login).get();
        if (user == null) {
            throw new BalanceTransactionException("Account not found " + login);
        }
        long newBalance = user.getAccount().getAmount() + amount;
        user.getAccount().setAmount(newBalance);
        log.warn("TRANSACTION FINISHES");
        return newBalance;
    }


}