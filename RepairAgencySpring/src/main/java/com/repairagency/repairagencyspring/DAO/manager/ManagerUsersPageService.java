package com.repairagency.repairagencyspring.DAO.manager;

import com.repairagency.repairagencyspring.DAO.BalanceDAO;
import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.entity.UserDB;
import com.repairagency.repairagencyspring.repos.UserRepository;
import com.repairagency.repairagencyspring.security.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Log4j2
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

    public HashMap<String,Object> addMoneyToUserFromManager(String money, String userName, HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        Locale locale=localeResolver.resolveLocale(request);
        result.put("status","ok");
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(locale).parse(money);
            long balance=balanceDAO.addAmount(userName, (long) (moneyValue.floatValue() * 100));
            result.put("money", resourceBundleMessageSource.getMessage("number.converter", new Float[]{((float)balance)/100},locale));
        } catch (ParseException | NullPointerException ex){
            result.put("status","error");
            result.put("message","parse");
        } catch (ConstraintViolationException ex){
            result.put("status","error");
            result.put("message","less");
        }
        return result;
    }

    @Transactional
    public void tryChangeLockUser(String userName, boolean locked){
        UserDB user = userRepository.findByLoginAndUserRoleNot(userName,Role.MANAGER).get();
        user.setLocked(locked);
        userRepository.save(user);
    }

    public HashMap<String, Object> lockUser(boolean locked, String userName){
        HashMap<String, Object> result = new HashMap<>();
        try{
            tryChangeLockUser(userName,locked);
        } catch (Exception ex) {
            locked=!locked;
        }
        result.put("status",locked);
        return result;
    }

}
