package com.repairagency.repairagencyspring.controller.user;

import com.repairagency.repairagencyspring.DAO.Exceptions.TaskNotFoundException;
import com.repairagency.repairagencyspring.DAO.Exceptions.UserNotFoundException;
import com.repairagency.repairagencyspring.DAO.RepoRedirectService;
import com.repairagency.repairagencyspring.dto.RepairTaskDTO;
import com.repairagency.repairagencyspring.entity.*;
import com.repairagency.repairagencyspring.DAO.BalanceDAO;
import com.repairagency.repairagencyspring.DAO.manager.ServiceAddPageService;
import com.repairagency.repairagencyspring.repos.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Log4j2
@Controller
@PreAuthorize("hasAnyAuthority('perm:user','perm:worker','perm:manager')")
@RequestMapping(value = "/account/user")
public class MainPageUserController {

    final LocaleResolver localeResolver;
    final UserRepository userRepository;
    final UserAccountRepository userAccountRepository;
    final RepairTaskRepository repairTaskRepository;
    final ServiceNameRepository serviceNameRepository;
    final FeedBackRepository feedBackRepository;
    final ResourceBundleMessageSource resourceBundleMessageSource;

    private final BalanceDAO balanceDAO;

    public MainPageUserController(LocaleResolver localeResolver,
                                  UserRepository userRepository,
                                  UserAccountRepository userAccountRepository,
                                  RepairTaskRepository repairTaskRepository,
                                  ServiceNameRepository serviceNameRepository,
                                  FeedBackRepository feedBackRepository, ResourceBundleMessageSource resourceBundleMessageSource, BalanceDAO balanceDAO) {
        this.userRepository = userRepository;
        this.userAccountRepository=userAccountRepository;
        this.localeResolver=localeResolver;
        this.repairTaskRepository=repairTaskRepository;
        this.serviceNameRepository=serviceNameRepository;
        this.feedBackRepository = feedBackRepository;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
        this.balanceDAO=balanceDAO;
    }

    @GetMapping("")
    public String userPage(@RequestParam(value = "errorValue",required = false) String value,
                           Model model,
                           Authentication authentication
                           ,@PageableDefault(page = 0, size = 10)
                               @SortDefault.SortDefaults({
                                       @SortDefault(sort = "dateCreate", direction = Sort.Direction.DESC),
                                       @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                               })
                                       Pageable pageable
                            )
    {
        if(value != null){
            model.addAttribute("errorMoney",value);
        }
        UserDB userDB = userRepository.findByLogin(authentication.getName()).get(); //User has already authenticated, so he is present in db
        Page<RepairTaskDTO> page = repairTaskRepository.findByOwner(userDB,pageable);
        final Iterable<ServiceName> services = serviceNameRepository.findAll(Sort.by(Sort.Order.asc("name")));

        long cents=userDB.getAccount().getAmount();

        model.addAttribute("page",page);
        model.addAttribute("url","user");
        model.addAttribute("services",services);
        model.addAttribute("userMoney",((float)cents)/100);

        return "account/user/usermainpage";
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
    public String addMoneyPage(@RequestParam(value = "money") String money, Authentication authentication, HttpServletRequest request)
    {
        String param="";
        try {
            final Number moneyValue = NumberFormat.getNumberInstance(localeResolver.resolveLocale(request)).parse(money);
            if(moneyValue.floatValue()>0) {
                balanceDAO.addAmount(authentication.getName(), (long) (moneyValue.floatValue() * 100));
            } else{
                param="?errorValue="+money;
            }
        } catch (ParseException | NullPointerException ignore){
            param="?errorValue="+money;
        }
        return "redirect:../user"+param;
    }

    @PostMapping("/addcomment/{id}")
    public String addCommentPage(@RequestParam(value = "feedBack") String feedBack, @PathVariable(value = "id") Long id, Authentication authentication){
        log.warn("test");
        if(feedBack.length()>512){
            feedBack=feedBack.substring(0,512);
        }
        try {
            RepairTask repairTask = repairTaskRepository.findByOwner_LoginAndIdAndWorkStatus(authentication.getName(), id, WorkStatus.DONE).orElseThrow(CommentCreateException::new);
            repairTask.setFeedBackMessage(feedBack);
            repairTaskRepository.save(repairTask);
        }catch (CommentCreateException ignore){

        }
        return "redirect:../../user";
    }

    @PostMapping("/getcomment/{id}")
    @ResponseBody
    public String getCommentPage( @PathVariable(value = "id") Long id){
        return repairTaskRepository.findById(id).orElseThrow(UserNotFoundException::new)
                .getFeedBack().getMessage();
    }

    @PostMapping("/trytopay/{id}")
    @ResponseBody
    public HashMap<String, Object> tryToPay(@PathVariable(value = "id") Long taskId, Authentication authentication,HttpServletRequest request){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","ok");
        result.put("id",taskId);
        try {
            RepairTask repairTask = repairTaskRepository.findByOwner_LoginAndIdAndPayStatus(authentication.getName(), taskId, PayStatus.WAIT).orElseThrow(TaskNotFoundException::new);
            UserAccount account = repairTask.getOwner().getAccount();
            Long price = repairTask.getPrice();
            Long amount=account.getAmount();
            if(price>amount){
                result.put("status","error");
                result.put("message",resourceBundleMessageSource.getMessage("user.money.not.enough",null,localeResolver.resolveLocale(request)));
            } else {
                account.setAmount(account.getAmount()-price);
                repairTask.setPayStatus(PayStatus.PAID);
                repairTaskRepository.save(repairTask);
                result.put("message",resourceBundleMessageSource.getMessage(PayStatus.PAID.getMessageId(),null,localeResolver.resolveLocale(request)));
            }
        } catch (TaskNotFoundException ignore){
            result.put("status","error");
            result.put("message","");
        }
        return result;
    }

}
