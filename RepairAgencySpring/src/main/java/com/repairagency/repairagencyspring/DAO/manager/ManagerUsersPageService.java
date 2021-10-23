package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.DAO.BalanceDAO;
import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import com.repairagency.repairagencyspring.security.Role;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerUsersPageService {

    private final UserRepository userRepository;
    private final LocaleResolver localeResolver;
    private final ResourceBundleMessageSource resourceBundleMessageSource;
    private final BalanceDAO balanceDAO;

    public ManagerUsersPageService(UserRepository userRepository, LocaleResolver localeResolver, ResourceBundleMessageSource resourceBundleMessageSource, BalanceDAO balanceDAO) {
        this.userRepository = userRepository;
        this.localeResolver = localeResolver;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
        this.balanceDAO = balanceDAO;
    }

    public Page<UserDTO> getPageUserDTO(Pageable pageable){
        List<UserDTO> listDto = userRepository.findAll(pageable).stream().map(UserDTO::new).collect(Collectors.toList());
        return new PageImpl<>(listDto,pageable,listDto.size());
    }

    public HashMap<String,Object> addMoneyManagerToUser(@NotNull String money, @NotNull String userName, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(localeResolver.resolveLocale(request)).parse(money);
            float value = moneyValue.floatValue();
            if(value>0) {
                long balance=balanceDAO.addAmount(userName, (long) (value * 100));
                result.put("money", resourceBundleMessageSource.getMessage("number.converter", new Float[]{((float)balance)/100},localeResolver.resolveLocale(request)));
            } else{
                result.put("status","error");
                result.put("message","less");
            }
        } catch (ParseException | NullPointerException ignore){
            result.put("status","error");
            result.put("message","parse");
        }
        return result;
    }

    public HashMap<String, Object> lockUser(boolean locked, String userName){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        Optional<UserDB> userDBOptional = userRepository.findByLogin(userName);
        if(userDBOptional.isPresent()){
            UserDB user = userDBOptional.get();
            if(user.getUserRole() == Role.MANAGER && locked){locked=false;}
            user.setLocked(locked);
            userRepository.save(user);
            result.put("message",locked);
        } else {
            result.put("status","false");
            result.put("message","notfound");
        }
        return result;
    }

}
