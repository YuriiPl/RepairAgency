package com.repairagency.repairagencyspring.DAO;

import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
public class BalanceDAO {

    private final UserRepository userRepository;

    @Autowired
    public BalanceDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public long addAmount(String login, long amount) throws BalanceTransactionException {
        log.warn("TRANSACTION STARTS");
        Optional<UserDB> userDBOptional = userRepository.findByLogin(login);
        if (!userDBOptional.isPresent()) {
            throw new BalanceTransactionException("Account not found " + login);
        }
        UserDB user = userDBOptional.get();
        long newBalance = user.getAccount().getAmount() + amount;
        user.getAccount().setAmount(newBalance);
        log.warn("TRANSACTION FINISHES");
        return newBalance;
    }


}