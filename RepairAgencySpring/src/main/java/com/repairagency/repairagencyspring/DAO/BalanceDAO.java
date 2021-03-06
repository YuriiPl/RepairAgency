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
    public long addAmount(String login, long amount) {
        return userRepository.findByLogin(login).get().getAccount().addMoney(amount);
    }


}