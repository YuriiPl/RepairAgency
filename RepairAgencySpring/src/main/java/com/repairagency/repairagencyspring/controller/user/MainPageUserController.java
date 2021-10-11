package com.repairagency.repairagencyspring.controller.user;

import com.repairagency.repairagencyspring.entity.UserAccount;
import com.repairagency.repairagencyspring.repos.UserAccountRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('perm:user','perm:worker','perm:manager')")
@RequestMapping(value = "/account/user")
public class MainPageUserController {

    final
    UserRepository userRepository;
    UserAccountRepository userAccountRepository;
    LocaleResolver localeResolver;

    public MainPageUserController(UserRepository userRepository,
                                  UserAccountRepository userAccountRepository,
                                  LocaleResolver localeResolver) {
        this.userRepository = userRepository;
        this.userAccountRepository=userAccountRepository;
        this.localeResolver=localeResolver;
    }

    @GetMapping("")
    public String userPage(@RequestParam(value = "errorValue",required = false) String value, Model model, Authentication authentication)
    {
        if(value != null){
            model.addAttribute("errorMoney",value);
        }
        long cents=userRepository.findByLogin(authentication.getName()).get().getAccount().getAmount();
        model.addAttribute("userMoney",((float)cents)/100);
        return "account/user/mainpage";
    }

    @PostMapping("/addmoney")
    //public @ResponseBody
    public String addMoneyPage(@RequestParam(value = "money") String money, Authentication authentication, HttpServletRequest request)
    {
        String param="";
        try {

            log.warn(request.getLocale().getLanguage());
            final Number moneyValue = NumberFormat.getNumberInstance(localeResolver.resolveLocale(request)).parse(money);
            if(moneyValue.floatValue()>0) {
                UserAccount userAccount = userRepository.findByLogin(authentication.getName()).get().getAccount();
                userAccount.addMoney((long) (moneyValue.floatValue() * 100));
                userAccountRepository.save(userAccount);
            } else{
                param="?errorValue="+money;
            }
        } catch (ParseException ignore){
            param="?errorValue="+money;
        }
        return "redirect:/account/user"+param;
    }

}
