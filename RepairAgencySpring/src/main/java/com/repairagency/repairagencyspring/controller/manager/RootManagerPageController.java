package com.repairagency.repairagencyspring.controller.manager;

import com.repairagency.repairagencyspring.DAO.BalanceDAO;
import com.repairagency.repairagencyspring.DAO.BalanceTransactionException;
import com.repairagency.repairagencyspring.dto.UserDTO;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@PreAuthorize("hasAuthority('perm:manager')")
@RequestMapping(value = "/account/manager")
public class RootManagerPageController {

    final UserRepository userRepository;
    private final BalanceDAO balanceDAO;
    final LocaleResolver localeResolver;
    final ResourceBundleMessageSource resourceBundleMessageSource;


    public RootManagerPageController(UserRepository userRepository,
                                     BalanceDAO balanceDAO,
                                     LocaleResolver localeResolver,
                                     ResourceBundleMessageSource resourceBundleMessageSource) {
        this.userRepository = userRepository;
        this.balanceDAO = balanceDAO;
        this.localeResolver = localeResolver;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @GetMapping("")
    public String managerPage()
    {
        return "redirect:/account/manager/tasks/new";
    }

    @GetMapping("/userslist")
    public String usersPage(
            Model model,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
//                    @SortDefault(sort = "serviceName.name", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "login", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable
    )
    {
        List<UserDTO> listDto = userRepository.findAll(pageable).stream().map(UserDTO::new).collect(Collectors.toList());
        Page <UserDTO> pageDto = new PageImpl<>(listDto,pageable,listDto.size());
        model.addAttribute("page",pageDto);
        model.addAttribute("url","userslist");

        return "account/manager/manageruserslistpage";
    }


    @PostMapping("/addmoney")
    @ResponseBody
    public HashMap<String, Object> addMoneyPage(@RequestParam(value = "money") String money, @RequestParam(value = "user") String userName, HttpServletRequest request)
    {
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
        } catch (ParseException | BalanceTransactionException ignore){
            result.put("status","error");
            result.put("message","parse");
        }
        return result;
    }

}
