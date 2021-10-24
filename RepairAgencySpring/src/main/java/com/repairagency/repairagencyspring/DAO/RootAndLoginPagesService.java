package com.repairagency.repairagencyspring.DAO;

import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RootAndLoginPagesService {
    private final UserRepository userRepository;

    public RootAndLoginPagesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCorrectPage(Authentication authentication, String basicPage){
        if(authentication != null) {
            final Optional<UserDB> user = userRepository.findByLogin(authentication.getName());
            if(user.isPresent()){
                return "redirect:"+user.get().getUserRole().getHomePage();
            }
        }
        return basicPage;
    }
}
