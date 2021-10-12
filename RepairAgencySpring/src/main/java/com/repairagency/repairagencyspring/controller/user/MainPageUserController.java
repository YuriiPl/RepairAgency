package com.repairagency.repairagencyspring.controller.user;

import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.*;
import com.repairagency.repairagencyspring.model.RepoRedirectService;
import com.repairagency.repairagencyspring.repos.RepairTaskRepository;
import com.repairagency.repairagencyspring.repos.ServiceNameRepository;
import com.repairagency.repairagencyspring.repos.UserAccountRepository;
import com.repairagency.repairagencyspring.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('perm:user','perm:worker','perm:manager')")
@RequestMapping(value = "/account/user")
public class MainPageUserController {

    final
    LocaleResolver localeResolver;
    UserRepository userRepository;
    UserAccountRepository userAccountRepository;
    RepairTaskRepository repairTaskRepository;
    ServiceNameRepository serviceNameRepository;

    public MainPageUserController(LocaleResolver localeResolver,
                                  UserRepository userRepository,
                                  UserAccountRepository userAccountRepository,
                                  RepairTaskRepository repairTaskRepository,
                                  ServiceNameRepository serviceNameRepository) {
        this.userRepository = userRepository;
        this.userAccountRepository=userAccountRepository;
        this.localeResolver=localeResolver;
        this.repairTaskRepository=repairTaskRepository;
        this.serviceNameRepository=serviceNameRepository;
    }

    @GetMapping("")
    public String userPage(@RequestParam(value = "errorValue",required = false) String value,
                           Model model,
                           Authentication authentication
                           ,@PageableDefault(page = 0, size = 10)
                               @SortDefault.SortDefaults({
                                       @SortDefault(sort = "serviceName.name", direction = Sort.Direction.ASC),
                                       @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                               })
                                       Pageable pageable
                            )
    {
        if(value != null){
            model.addAttribute("errorMoney",value);
        }
        UserDB userDB = userRepository.findByLogin(authentication.getName()).get(); //User has already authenticated, so anyway he is present in db
        Page<RepairTaskDTO> page = repairTaskRepository.findByOwner(userDB,pageable);
        final Iterable<ServiceName> services = serviceNameRepository.findAll(Sort.by(Sort.Order.asc("name")));

        long cents=userDB.getAccount().getAmount();

        model.addAttribute("page",page);
        model.addAttribute("url","user");
        model.addAttribute("services",services);
        model.addAttribute("userMoney",((float)cents)/100);

        return "account/user/mainpage";
    }


    @PostMapping("/tasks/add")
    public String addRepairTask(@RequestParam(value = "serviceId") Long serviceId,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication){
        RepairTaskDTO repairTaskDTO = new RepairTaskDTO();
        try {
            repairTaskDTO.setServiceName(serviceNameRepository.findById(serviceId).orElseThrow(RuntimeException::new));
        } catch (RuntimeException ex){
            return "redirect:/account/user"; //params.....
        }
        UserDB userDB = userRepository.findByLogin(authentication.getName()).get(); //User has already authenticated, so anyway he is present in db
        repairTaskDTO.setOwner(userDB);
        repairTaskDTO.setWorkStatus(WorkStatus.FREE);
        repairTaskDTO.setPayStatus(PayStatus.FREE);
        repairTaskDTO.setDateCreate(LocalDateTime.now());
        return RepoRedirectService.save(repairTaskRepository,new RepairTask(repairTaskDTO),"../../user", request,redirectAttributes);
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

    @PostMapping("/addcomment/{id}")
    public String addCommentPage(@RequestParam(value = "feedBack") String feedBack, @PathParam(value = "id") Long id){
        
    }

}
